package io.seg.kofo.bitcoinwo.biz.job;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import io.seg.kofo.bitcoinwo.biz.service.BtcBlockHeightService;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.config.WatchOnlyProperties;
import io.seg.kofo.bitcoinwo.dao.po.BtcBlockHeightPo;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;


/**
 * 更新本地全节点高度
 * 回调高度
 * @author gin
 */
@Slf4j
@Component
public class BlockHeightUpdateJob {
    @Autowired
    BtcBlockHeightService blockHeightService;
    @Autowired
    WatchOnlyProperties watchOnlyProperties;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FullNodeCache fullNodeCache;

    @Scheduled(initialDelay = 10000, fixedDelay = 25000)
    public void execute() {
        try {
            //默认只有一条记录
            BtcBlockHeightPo btcBlockHeightPo = blockHeightService.selectOne(BtcBlockHeightPo.builder().build());
            updateNodeHeight(btcBlockHeightPo);
            //todo 测试网络暂停更新该值 正式网络需要打开
//            updateLatestHeight(btcBlockHeightPo);
            checkNodeHeight();

        } catch (Exception e) {
            log.error("BlockHeightUpdateJob exception:{}",e.getMessage(),e);
        }
    }

    /**
     * 同时检查omni和bitcoin节点的高度，相差3个高度以上则报警
     * @return
     */
    private boolean checkNodeHeight(){
        try {
            int btcNodeCount = fullNodeCache.getBitcoinClient().getBlockCount();
            int omniNodeCount = fullNodeCache.getOmniClient().getBlockCount();
            if (Math.abs(btcNodeCount - omniNodeCount) > watchOnlyProperties.getLagThreshold()){
                //todo 如果两个节点高度相差太多则报警
                log.warn(">>lagged<< btc-node:[{}] or omni-node:[{}] lagged",btcNodeCount,omniNodeCount);
            }
        } catch (BitcoinException e) {
            log.error("rpc failed:{}",e.getMessage(),e);
        }
        return false;
    }

    private boolean updateNodeHeight(BtcBlockHeightPo btcBlockHeightPo){
        try {
            int blockCount = fullNodeCache.getBitcoinClient().getBlockCount();
            if (btcBlockHeightPo.getLatestBlockHeight() - blockCount > watchOnlyProperties.getLagThreshold()){
                //todo 节点高度落后 报警。
                log.warn(">>lagged<< latestBlockHeight too much more than nodeHeight.");
            }
            //if (btcBlockHeightPo.getNodeLatestBlockHeight() < blockCount){
                //更新本地节点高度
                btcBlockHeightPo.setNodeLatestBlockHeight((long) blockCount);
                btcBlockHeightPo.setUpdateTime(new Date());
                blockHeightService.update(btcBlockHeightPo);
                log.info("update btcNodeHeight to:{}",btcBlockHeightPo.getNodeLatestBlockHeight());
            //}
        }catch (Exception e){
            log.warn("updateNodeHeight exception:{}",e.getMessage(),e);
        }
        return true;

    }

    private boolean updateLatestHeight(BtcBlockHeightPo btcBlockHeightPo){
        try {
            JSONObject response = restTemplate.getForEntity(watchOnlyProperties.getBtcLatestBlockUrl(), JSONObject.class).getBody();
            if (null != response){
                //更新最高区块高度
                long latestHeight = (Integer) ((Map)response.get("data")).get("height");
                if (btcBlockHeightPo.getLatestBlockHeight() < latestHeight){
                    btcBlockHeightPo.setLatestBlockHeight(latestHeight);
                    btcBlockHeightPo.setUpdateTime(new Date());
                    blockHeightService.update(btcBlockHeightPo);
                    log.info("update latestHeight to:{}",btcBlockHeightPo);
                }
            }
        }catch (Exception e){
            log.warn("updateLatestHeight exception:{}",e.getMessage(),e);
        }
        return true;
    }
}


