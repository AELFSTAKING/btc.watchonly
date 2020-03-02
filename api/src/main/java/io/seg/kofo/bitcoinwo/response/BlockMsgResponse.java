package io.seg.kofo.bitcoinwo.response;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BlockMsgResponse {

    /**
     * 高度
     */
    private Long height;
    /**
     * 区块hash
     */
    private String blockHash;
    /**
     * 区块内容
     */
    private String msg;


}
