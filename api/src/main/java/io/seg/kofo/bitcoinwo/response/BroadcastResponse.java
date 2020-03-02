package io.seg.kofo.bitcoinwo.response;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BroadcastResponse {

    /**
     * 币种
     */
    private String currencyName;
    /**
     * 交易hash
     */
    private String txHash;
    /**
     * 广播结果
     */
    private String status;
    /**
     * 广播操作时间
     */
    private String timestamp;

}
