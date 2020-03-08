

package io.seg.kofo.bitcoinwo.biz.service.impl;

import com.google.common.collect.Lists;
import io.seg.framework.dao.model.Order;
import io.seg.framework.dao.model.QueryResult;
import io.seg.kofo.bitcoinwo.biz.service.BlockCacheService;
import io.seg.kofo.bitcoinwo.biz.service.BtcBlockHeightService;
import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.biz.service.SyncHeightService;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.config.WatchOnlyProperties;
import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.BtcBlockHeightPo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.dao.po.SyncHeightPo;
import io.seg.kofo.bitcoinwo.enums.MsgTypeEnum;
import com.alibaba.fastjson.JSON;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import io.seg.framework.dao.BaseDao;
import io.seg.kofo.bitcoinwo.model.bo.BlockInfo;
import io.seg.kofo.bitcoinwo.model.bo.OmniTx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author gin
 */
@Slf4j
@Service
public class MsgQueueServiceImpl implements MsgQueueService {
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private WatchOnlyProperties watchOnlyProperties;
    @Autowired
    private MsgQueueService msgQueueService;
    @Autowired
    private BlockCacheService blockCacheService;
    @Autowired
    private SyncHeightService syncHeightService;
    @Autowired
    private BtcBlockHeightService btcBlockHeightService;
    @Autowired
    private FullNodeCache fullNodeCache;

    @Override
    public MsgQueuePo selectOne(MsgQueuePo msgQueue) {
        return baseDao.selectOne(msgQueue);
    }

    @Override
    public QueryResult<MsgQueuePo> selectLimit(MsgQueuePo msgQueue) {
        return baseDao.selectQueryResult(msgQueue, msgQueue.getPageIndex(), msgQueue.getPageSize());
    }

    @Override
    public List<MsgQueuePo> selectList(MsgQueuePo msgQueue) {
        return baseDao.selectList(msgQueue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MsgQueuePo msgQueue) {
        return baseDao.insert(msgQueue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MsgQueuePo msgQueue) {
        return baseDao.update(msgQueue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MsgQueuePo set, MsgQueuePo where) {
        return baseDao.update(set, where);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(MsgQueuePo msgQueue) {
        return baseDao.delete(msgQueue);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchInsert(List<MsgQueuePo> list) {
        return baseDao.batchInsert(MsgQueuePo.class, list);
    }

    @Override
    public int countMsgQueue() {
        return baseDao.count(MsgQueuePo.builder().build());
    }


    @Override
    public MsgQueuePo generateBlockMsg(Bitcoin.Block syncBlock)    {
        log.info("begin request blockhash:{},height:{}", syncBlock.hash(), syncBlock.height());

        Bitcoin bitcoinClient = fullNodeCache.getBitcoinClient();
        Bitcoin omniClient = fullNodeCache.getOmniClient();
        List<String> txHashList = syncBlock.tx();
        List<String> rawTxList = new ArrayList<>();
        //获取rawTx
        try {
            for (String txHash : txHashList) {
                rawTxList.add(bitcoinClient.getRawTransactionHex(txHash));
            }
            //获取omniTx list
            List<String> omniTxHashList = omniClient.omniListBlockTransactions(syncBlock.height());
            List<OmniTx> omniTxList = new ArrayList<>();
            for (String omniTxHash : omniTxHashList) {
                try {
                    Bitcoin.OmniTransaction omniTransaction = omniClient.omniGettransaction(omniTxHash);
                    OmniTx tx = OmniTx.builder()
                            .blockHash(syncBlock.hash())
                            .blockTime(syncBlock.time().getTime())
                            .height(syncBlock.height())
                            .fee(omniTransaction.fee())
                            .txid(omniTransaction.txid())
                            .positionInBlock(omniTransaction.positionInBlock())
                            .referenceAddress(omniTransaction.referenceaddress())
                            .sendingAddress(omniTransaction.sendingaddress())
                            .valid(omniTransaction.valid())
                            .invalidReason(omniTransaction.invalidreason())
                            .type(omniTransaction.type())
                            .typeInt(omniTransaction.typeInt())
                            .version(omniTransaction.version())
                            .originJson(omniTransaction.originJson())
                            .build();
                    omniTxList.add(tx);
                }catch (Exception e){
                    log.error("omniTx request error :{} ,skip tx:{}",e.getMessage(),omniTxHash,e);
                }

            }
            //生成区块信息寸库
            //rpc调用中缺少strippedsize,mediantime,versionHex
            BlockInfo blockInfo = BlockInfo.builder()
                    .bits(syncBlock.bits())
                    .confirmations(syncBlock.confirmations())
                    .difficulty((long) syncBlock.difficulty())
                    .hash(syncBlock.hash())
                    .height(syncBlock.height())
                    .time(syncBlock.time().getTime())
                    .merkleroot(syncBlock.merkleRoot())
                    .nonce((int) syncBlock.nonce())
                    .previousblockhash(syncBlock.previousHash())
                    .size(syncBlock.size())
                    .tx(rawTxList)
                    .omniTx(omniTxList)
                    .version(syncBlock.version())
                    .build();
            MsgQueuePo msgQueuePo = MsgQueuePo.builder()
                    //保存json串
                    .msg(JSON.toJSONString(blockInfo))
                    .msgType(MsgTypeEnum.BLOCK_MSG.getCode())
                    .isCallback(false)
                    .createTime(new Date())
                    .height((long) syncBlock.height())
                    .blockHash(syncBlock.hash())
                    .build();

            return msgQueuePo;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBlockInfo(MsgQueuePo msgQueuePo) {
        if (msgQueuePo.getMsgType().equals(MsgTypeEnum.FORKING_MSG.getCode())){
            //分叉处理
            //删除blockCache中缓存记录(fork_height之后的)
            int count = 0;
            SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
            long preSyncHeight = syncHeightPo.getSyncHeight();
            for (int i = Math.toIntExact(msgQueuePo.getHeight()); i < preSyncHeight; i++) {
                count += blockCacheService.delete(BlockCachePo.builder()
                        .height((long) (i + 1))
                        .build());
            }
            boolean isDeleteBlockCache = count == preSyncHeight - msgQueuePo.getHeight();
            if (!isDeleteBlockCache) {
                throw new RuntimeException("isDeleteBlockCache generateForkingMsg Exception");
            }
            /**
             * 需要回调fork消息
             */
            msgQueueService.insert(msgQueuePo);
        }

        List<BlockCachePo> cachedBlock = blockCacheService.selectLimit(BlockCachePo.builder()
                .blockHash(msgQueuePo.getBlockHash())
                .pageIndex(1)
                .pageSize(1)
                .build()).getRows();
        boolean isInsertBlockCache = true;
        if (cachedBlock.size() <= 0) {
            BlockCachePo blockCachePo = BlockCachePo.builder()
                    .height(msgQueuePo.getHeight())
                    .blockHash(msgQueuePo.getBlockHash())
                    .build();
            //不存在该hash则插入
            isInsertBlockCache = blockCacheService.insert(blockCachePo) == 1;
        }
        //todo 不需要消息队列了
//        boolean isInsertMsg = msgQueueService.insert(msgQueuePo) == 1;
        if (isInsertBlockCache ) {
            return true;
        } else {
            throw new RuntimeException("saveBlockInfo exception");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MsgQueuePo generateForkingMsg(Bitcoin.Block forkingBlock) {
        //默认只有一条记录
        SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());

        //caution 该forkingBlock高度最大有可能是syncHeight - 1 而不可能等于syncHeight
        if (forkingBlock.height() < syncHeightPo.getSyncHeight()) {
            log.warn("forking from {} to {}", syncHeightPo.getSyncHeight(), forkingBlock.height());

        } else {
            throw new RuntimeException("forking block height larger than syncHeight");
        }
        //生成分叉消息 msg内容为fork_height
        MsgQueuePo msgQueuePo = MsgQueuePo.builder()
                .isCallback(false)
                .msgType(MsgTypeEnum.FORKING_MSG.getCode())
                .createTime(new Date())
                .msg(String.valueOf(forkingBlock.height() ))
                .height((long) forkingBlock.height() )
                .blockHash(forkingBlock.hash())
                .build();
//        isInsertMsg = msgQueueService.insert(msgQueuePo) == 1;

        return msgQueuePo;
    }

    @Override
    public List<MsgQueuePo> getMsgForCallBack() {
        MsgQueuePo msgQueuePo  = MsgQueuePo.builder()
                .isCallback(false)
                .orderBy(Lists.newArrayList(Order.asc("id")))
                .pageSize(1)
                .pageIndex(1)
                .build();
        return baseDao.selectQueryResult(msgQueuePo, msgQueuePo.getPageIndex(), msgQueuePo.getPageSize()).getRows();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCallBackInfo(MsgQueuePo updatedMsg) {
        updatedMsg.setIsCallback(true);
        updatedMsg.setUpdateTime(new Date());
        updatedMsg.setLastCallbackTime(new Date());
        boolean isUpdateMsg = msgQueueService.update(updatedMsg) == 1;
        BtcBlockHeightPo blockHeightPo = btcBlockHeightService.selectOne(BtcBlockHeightPo.builder().build());
        blockHeightPo.setLastCallBackHeight(updatedMsg.getHeight());
        blockHeightPo.setLastCallBackTime(new Date());
        boolean isUpdateBlockHeight = btcBlockHeightService.update(blockHeightPo) == 1;
        if (isUpdateBlockHeight && isUpdateMsg) {
            return true;
        } else {
            throw new RuntimeException("saveBlockInfo exception");
        }
    }


}
