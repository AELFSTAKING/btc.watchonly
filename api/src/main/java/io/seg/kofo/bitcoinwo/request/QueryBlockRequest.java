package io.seg.kofo.bitcoinwo.request;

import lombok.*;

/**
 * 通过高度查询区块信息
 */
@Setter
@Getter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryBlockRequest {
//    通过block height 查询
    private Long height;

}
