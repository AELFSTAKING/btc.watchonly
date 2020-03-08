package io.seg.kofo.bitcoinwo.biz.job;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.google.common.collect.Lists;
import io.seg.kofo.bitcoinwo.biz.service.*;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.config.WatchOnlyProperties;
import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.BtcBlockHeightPo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.dao.po.SyncHeightPo;
import io.seg.kofo.bitcoinwo.model.bo.BlockJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * 检查消息队列是否有空位
 * 有空位则流处理同步高度同时生成消息
 *
 * @author gin
 */
@Slf4j
@Component
public class MsgGeneratorDataFlowJob {
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BlockMsgService blockMsgService;
    @Autowired
    BlockCacheService blockCacheService;
    @Autowired
    BtcBlockHeightService blockHeightService;
    @Autowired
    SyncHeightService syncHeightService;
    @Autowired
    WatchOnlyProperties watchOnlyProperties;
    @Autowired
    FullNodeCache fullNodeCache;

    private int concurrentThread = 2;

    AtomicInteger atomicInteger = new AtomicInteger();


    Executor executor ;

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void syncBlock() {
        int processCount = 0;
        log.info("sync block start:");

        while (true) {
            List<SyncHeightPo> list = fetchData();

            if (Objects.isNull(list) || list.isEmpty()) {
                log.info("sync block end,process {} block.", processCount);
                break;
            }
            processCount++;
            list.forEach(this::processData);
        }
    }



    @PostConstruct
    public void setUp(){
        executor = Executors.newFixedThreadPool(concurrentThread,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        //使用守护线程,使用这种方式不会组织程序的关停
                        thread.setDaemon(true);
                        thread.setName("block-api-request-"+atomicInteger.getAndIncrement());
                        return thread;
                    }
                }
        );
    }

    public List<SyncHeightPo> fetchData() {
        Integer msgQueueLimit = Integer.valueOf(watchOnlyProperties.getMsgQueueLimit());
        if (msgQueueLimit <= msgQueueService.countMsgQueue()) {
            return new ArrayList();
        }
        BtcBlockHeightPo btcBlockHeightPo = blockHeightService.selectOne(BtcBlockHeightPo.builder().build());
        //表中只有一条数据
        SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
        if (syncHeightPo.getSyncHeight() >= btcBlockHeightPo.getNodeLatestBlockHeight()) {
            //同步高度大于全节点最高高度则不同步
            return new ArrayList();
        }
        log.info("ready to syncHeight from :{} to:{}", syncHeightPo.getSyncHeight(), syncHeightPo.getSyncHeight() + 1);
        //获取当前已同步到的高度进行后续处理
        return Lists.newArrayList(syncHeightPo);
    }

    public void processData(SyncHeightPo preSyncHeight) {
        long originHeight = preSyncHeight.getSyncHeight();
        BtcBlockHeightPo btcBlockHeightPo = blockHeightService.selectOne(BtcBlockHeightPo.builder().build());
        /**
         * 批量处理，先按顺序获取一组blockHeader list
         * 然后多线程请求tx填满这一组block。
         * 再插入msg队列等待回调。
         */
        long before = System.currentTimeMillis();
        Bitcoin bitcoin = null;
        List<BlockJob> blockJobList = new ArrayList<>();
        try {
            for (int i = 0; i < 10 ; i++){
                if (preSyncHeight.getSyncHeight() >= btcBlockHeightPo.getNodeLatestBlockHeight()){
                    break;
                }
                    bitcoin = fullNodeCache.getBitcoinClient();

                    Bitcoin.BlockHeader header = bitcoin.getBlockHeaderAtHeight(Math.toIntExact(preSyncHeight.getSyncHeight() + 1));
                    //前一个hash不等于preSyncHeight的hash
                    boolean isPreSyncHash = header.previousHash().equals(preSyncHeight.getBlockHash());
                    boolean isForking = false;
                    BlockCachePo preBlockCachePo = null;

                    if (!isPreSyncHash) {
                        //可能preSyncHash没有填写hash 但也可以依赖cache表进行查找
                        //依赖blockCache判断追溯分叉点,第一次查询数据库可以避免网络请求
                        //要求blockCache必须有缓存区块信息。
                        preBlockCachePo = blockCacheService.selectOne(BlockCachePo.builder().blockHash(
                                header.previousHash()
                        ).build());
                        if (null == preBlockCachePo) {
                            // preSyncHash和blockCache表均未查询到，判断为分叉
                            isForking = true;
                        }
                    }
                    if (isForking) {
                        //todo 分叉报警？
                        //开始网络请求追溯分叉点
                        Bitcoin.Block preBlock = header.previous();
                        while (null == preBlockCachePo) {
                            //前溯一个区块--查询blockCache中不存在的区块,直到有blockCache记录-找到分叉点
                            preBlock = preBlock.previous();
                            log.info("forking getPreBlock height:{},hash:{}", preBlock.height(), preBlock.hash());

                            //todo blockcache为空应该报警处理否则死循环。
                            preBlockCachePo = blockCacheService.selectOne(BlockCachePo.builder().blockHash(
                                    preBlock.hash()
                            ).build());
                        }

                        //caution BTC的分叉区块是指最后一个共有块 与eth逻辑区分开
                        Bitcoin.Block forkBlock = bitcoin.getBlock(preBlockCachePo.getBlockHash());
                        log.info("forking blockhash:{},height:{}", preBlockCachePo.getBlockHash(), forkBlock.height());
                        blockJobList.add(BlockJob.builder()
                                .isFork(true)
                                .block(forkBlock)
                                .build());
                        preSyncHeight.setBlockHash(forkBlock.hash());
                        preSyncHeight.setSyncHeight((long) forkBlock.height());
                        //碰到分叉信息后需要结束循环 跟下面队列排序相关，fork信息放在最末。
                        //todo 应当在检测到分叉信息后 删除高度大于分叉块的msg。？？
                        break;
                    } else {
                        Bitcoin.Block syncBlock = bitcoin.getBlock(header.hash());
                        log.info("syncHeight blockhash:{},height:{}", syncBlock.hash(), syncBlock.height());
                        blockJobList.add(BlockJob.builder()
                                .isFork(false)
                                .block(syncBlock)
                                .build());
                        preSyncHeight.setBlockHash(syncBlock.hash());
                        preSyncHeight.setSyncHeight((long) syncBlock.height());
                    }
            }
        // 将得到的一组block header 进行并发填充。
            List<CompletableFuture<MsgQueuePo>> futures =
                    blockJobList.parallelStream().map(
                            blockJob -> CompletableFuture.supplyAsync(
                                    () -> blockJob.isFork() ? msgQueueService.generateForkingMsg(blockJob.getBlock())
                                            :msgQueueService.generateBlockMsg(blockJob.getBlock()),
                            executor)).collect(Collectors.toList());
            List<MsgQueuePo> msgQueuePoList = futures.stream()
                    //join与future.get类似但不会抛异常。
                    .map(CompletableFuture::join).collect(Collectors.toList());
            if(msgQueuePoList.size() != blockJobList.size()){
                throw new RuntimeException("block request does not complete!");
            }
            // 排序 mgs入库
            //fork信息放在队列最末 优先根据msgType倒序，然后根据height信息正序排列
            msgQueuePoList.sort(Comparator.comparing(MsgQueuePo::getMsgType).reversed().thenComparing(MsgQueuePo::getHeight));
            //更新syncHeight表和blockCache表
            for (MsgQueuePo msgQueuePo : msgQueuePoList){
                msgQueueService.saveBlockInfo(msgQueuePo);
                /**
                 * 保存msg信息 以便之后gateway直接从wo的db中读取区块 ，充当全节点的proxy
                 */
                blockMsgService.saveBlockMsg(msgQueuePo);
                // 更新同步到的高度
                syncHeightService.updateSyncHeight(msgQueuePo);
            }

            long after = System.currentTimeMillis();
            log.info("syncHeight finish,from {},to {} ,processCount:{} spend:{} ms", originHeight, preSyncHeight.getSyncHeight(), msgQueuePoList.size(),after - before);

        } catch (Exception e) {
            log.error("MsgGeneratorDataFlowJob processData exception:{}", e.getMessage(), e);
        }
    }

}
