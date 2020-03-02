package io.seg.kofo.bitcoinwo.common.config;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.google.common.cache.*;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.seg.kofo.bitcoinwo.biz.service.BtcBlockHeightService;
import io.seg.kofo.bitcoinwo.common.util.BitcoinRpcURLWrapper;
import io.seg.kofo.bitcoinwo.dao.po.BtcBlockHeightPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 1.在原有节点不lag的情况下 继续使用原有节点
 *
 * @author gin
 */
@Slf4j
@Component
public class FullNodeCache {

    public static final String BEST_OMNI_NODE = "bestOmniNode";
    public static final String BEST_BTC_NODE = "bestBtcNode";

    private final WatchOnlyProperties watchOnlyProperties;

    private final BtcBlockHeightService blockHeightService;


    private LoadingCache<String, String> fullNodeCache = CacheBuilder.newBuilder()
            .maximumSize(2)
            .concurrencyLevel(5)
            .refreshAfterWrite(30, TimeUnit.SECONDS)
//            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    log.info("load full-node cache for {}", key);
                    return loadBest(key,null);
                }

                @Override
                public ListenableFuture<String> reload(String key, String oldValue) throws Exception {
                    log.info("reload....key:{},value:{}", key, oldValue);
                    BtcBlockHeightPo blockHeightPo = blockHeightService.selectOne(BtcBlockHeightPo.builder().build());
                    String currentNodeString = oldValue;
                    Bitcoin currentNode = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(watchOnlyProperties.getRpcUser(key), watchOnlyProperties.getRpcPassword(key), currentNodeString);
                    int currentNodeHeight = currentNode.getBlockCount();
                    checkOutterLatestHeight(blockHeightPo.getLatestBlockHeight(), (long) currentNodeHeight,currentNodeString);
                    return Futures.immediateFuture(loadBest(key,oldValue));
                }
            });

    @Autowired
    public FullNodeCache(WatchOnlyProperties watchOnlyProperties, BtcBlockHeightService blockHeightService) {
        this.watchOnlyProperties = watchOnlyProperties;
        this.blockHeightService = blockHeightService;
    }


    public LoadingCache<String, String> getFullNodeCache() {
        return fullNodeCache;
    }

    /**
     * 兼具初次加载/反复刷新的功能
     * @param key
     * @param oldValue optional
     * @return
     * @throws Exception
     */
    public String loadBest(String key,String oldValue) throws Exception {

        //表中只有一条height记录
        BtcBlockHeightPo blockHeightPo = blockHeightService.selectOne(BtcBlockHeightPo.builder().build());
        //先判断原有的cache需不需要刷新
        String fullNodes;
        switch (key) {
            case BEST_BTC_NODE:
                fullNodes = watchOnlyProperties.getBtcNodes();
                break;
            case BEST_OMNI_NODE:
                fullNodes = watchOnlyProperties.getOmniNodes();
                break;
            default:
                throw new RuntimeException("Unknown key for full node.>>" + key);
        }

        String[] fullNodeGroup = fullNodes.split(",");
        Map<String, Long> availableNodeHeight = new HashMap<>();
        long heighest = 0;
        String heighestNode = null;
        //遍历请求各个节点高度 同时选择最高节点
        for (String nodeUrlString : fullNodeGroup) {
            try {
                long blockCount = getValideNodeHeight(blockHeightPo, nodeUrlString, key);
                availableNodeHeight.put(nodeUrlString, blockCount);
                if (blockCount > heighest) {
                    heighest = blockCount;
                    heighestNode = nodeUrlString;
                }
            } catch (Exception e) {
                //todo 报警，一个node不可用
                log.error(">>>node unavailable<<< node:[{}]", nodeUrlString);
            }
        }
        log.info("available node info:{}", availableNodeHeight);
        if (heighestNode == null) {
            throw new RuntimeException("heighestNode is null");
        }
        if (oldValue != null){
            String originNode = oldValue;
            Bitcoin currentNode = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(watchOnlyProperties.getRpcUser(key), watchOnlyProperties.getRpcPassword(key), originNode);
            try {
                int originNodeHeight = currentNode.getBlockCount();
                if ( heighest - originNodeHeight < watchOnlyProperties.getLagThreshold() &&  heighest - originNodeHeight >= 0 ){
                    //如果最高节点高度-原节点高度 大于等于0 小于阈值 则使用原节点
                    log.info("origin node:{}, height:{} heighest:{},lagthreshold:{},no neeed refresh",originNode,originNodeHeight,heighest,watchOnlyProperties.getLagThreshold());
                    return originNode;
                }else {
                    // heighest  < originNodeHeight
                    // heighest - originNodeHeight > threshold
                }
            }catch (Exception  e){
                log.error("originNode:{} shut down!",originNode);
            }

        }




        return heighestNode;
    }

    /**
     * 检查外部节点高度
     * 报警处理
     */
    private void checkOutterLatestHeight(Long outterLatestBlockHeight,Long currentNodeHeight,String currentNodeString) {
        if (outterLatestBlockHeight - currentNodeHeight <= watchOnlyProperties.getLagThreshold()) {
            if (outterLatestBlockHeight - currentNodeHeight >= 0) {
                //如果与外部节点高度相差不大
                log.info("latestHeight:{} current Node :{} height:{} lagged less than threshold:{} , no need refresh fullNodeCache", outterLatestBlockHeight, currentNodeString, currentNodeHeight, watchOnlyProperties.getLagThreshold());
            } else {
                log.error(">>>lagged<<< latestHeight :{} far less than current node :{} height:{},outer-node or current-node maybe wrong", outterLatestBlockHeight, currentNodeString, currentNodeHeight);
            }
        } else {
            log.warn(">>>lagged<<< latestHeight:{} current Node :{} height:{} lagged more than threshold:{},should refresh fullNodeCache", outterLatestBlockHeight, currentNodeString, currentNodeHeight, watchOnlyProperties.getLagThreshold());
        }
    }

    private int getValideNodeHeight(BtcBlockHeightPo btcBlockHeightPo, String nodeUrlString, String key) throws Exception {
        Bitcoin bitcoinClient = null;
        try {
            bitcoinClient = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(watchOnlyProperties.getRpcUser(key), watchOnlyProperties.getRpcPassword(key), nodeUrlString);
        } catch (MalformedURLException e) {
            log.error("node url exception:[{}]", nodeUrlString);
            throw e;
        }
        int blockCount;
        try {
            blockCount = bitcoinClient.getBlockCount();
        } catch (BitcoinException e) {
            log.error("request node height error for url:{} ,error:{} ", nodeUrlString, e.getMessage(), e);
            throw e;
        }
        //先检查与外部最新高度的差距
        if (Math.abs(btcBlockHeightPo.getLatestBlockHeight() - blockCount) > watchOnlyProperties.getLagThreshold()) {
            log.error(">>>>lagged<<<< node:{} latestBlockHeight:{}, nodeBlockCount:{}", nodeUrlString, btcBlockHeightPo.getLatestBlockHeight(), blockCount);
        }
        return blockCount;
    }

    public Bitcoin getBitcoinClient() {
        try {
            Bitcoin btcClient = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(watchOnlyProperties.getBtcRpcUser(), watchOnlyProperties.getBtcRpcPassword(), fullNodeCache.get(BEST_BTC_NODE));
            return btcClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Bitcoin getOmniClient() {
        try {
            Bitcoin omniClient = BitcoinRpcURLWrapper.bitcoinJSONRPCClient(watchOnlyProperties.getOmniRpcUser(), watchOnlyProperties.getOmniRpcPassword(), fullNodeCache.get(BEST_OMNI_NODE));
            return omniClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
