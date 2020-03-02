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
public class QueryIdRequest {
//    查询交易id
    private String queryId;
}
