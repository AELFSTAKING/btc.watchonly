package io.seg.kofo.bitcoinwo.common.config;


import lombok.Data;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "watch-only")
@Data
@RefreshScope
public class WatchOnlyProperties {


    /**
     * 获取外部最高高度的url 如: https://chain.api.btc.com/v3/block/latest
     */
    private String btcLatestBlockUrl;

    /**
     * 网络参数 解析交易需要
     */
    private String parameters;
    /**
     * btc rpc调用fullnode 逗号分隔开例如: 127.0.0.1:18332,127.0.0.1:18332
     */
    private String btcNodes;
    private String btcRpcUser;
    private String btcRpcPassword;

    /**
     * omni rpc调用fullnode 逗号分隔开： 127.0.0.1:28332,127.0.0.1:28332
     */
    private String omniNodes;
    private String omniRpcUser;
    private String omniRpcPassword;


    /**
     * 消息队列长度
     */
    private String msgQueueLimit;

    /**
     * 允许的节点滞后数值
     */
    private int lagThreshold;


    public NetworkParameters getParameters() {
        switch (this.parameters) {
            case "regtest":
                return RegTestParams.get();
            case "mainnet":
                return MainNetParams.get();
            /**
             * 公网测试网络
             */
            case "testnet":
                return TestNet3Params.get();
            default:
                /**
                 * 默认私有测试网络
                 */
                return RegTestParams.get();
        }
    }

    /**
     * 便于fullnodecache获取
     * @param cacheKey
     * @return
     */
    public String getRpcUser(String cacheKey) {
        switch (cacheKey) {
            case FullNodeCache.BEST_BTC_NODE:
                return this.btcRpcUser;
            case FullNodeCache.BEST_OMNI_NODE:
                return this.omniRpcUser;
            default:
                return this.btcRpcUser;
        }

    }

    /**
     * 便于fullnodecache获取
     * @param cacheKey
     * @return
     */
    public String getRpcPassword(String cacheKey) {
        switch (cacheKey) {
            case FullNodeCache.BEST_BTC_NODE:
                return this.btcRpcPassword;
            case FullNodeCache.BEST_OMNI_NODE:
                return this.omniRpcPassword;
            default:
                return this.btcRpcPassword;
        }

    }


}
