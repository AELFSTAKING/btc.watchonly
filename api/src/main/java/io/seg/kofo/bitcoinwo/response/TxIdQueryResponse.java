package io.seg.kofo.bitcoinwo.response;

import lombok.*;

/**
 * @author yangjiyun
 * @Description: ${todo}
 * @date 2018/7/8 23:46
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TxIdQueryResponse {

    private String rawTransaction;

    private String blockHash;

    private int blockHeight;

    private int confirmations;
}
