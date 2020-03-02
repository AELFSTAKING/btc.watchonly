package io.seg.kofo.bitcoinwo.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class BroadcastRequest {

    /**
     * 币种信息
     */
    @NotBlank
    private String currencyName;
    /**
     * 交易哈希
     */
    @NotBlank
    private String txHash;
    /**
     * 带签名的交易原始数据
     */
    @NotBlank
    private String rawTransaction;

}
