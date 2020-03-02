package io.seg.kofo.bitcoinwo.request;

import lombok.*;

/**
 * @author yangjiyun
 * @Description:
 * @date 2018/10/15 16:38
 */
@Setter
@Getter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawTransactionRequest {
    //    请求中的原始交易HEX串
    private String rawTransaction;
}
