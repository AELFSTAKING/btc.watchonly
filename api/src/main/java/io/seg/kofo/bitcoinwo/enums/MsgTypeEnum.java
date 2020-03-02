package io.seg.kofo.bitcoinwo.enums;

import lombok.Getter;

@Getter
public enum MsgTypeEnum {
    /**
     * 分叉消息
     */
    FORKING_MSG(0, "forking message"),
    /**
     * 区块消息
     */
    BLOCK_MSG(1, "block message"),
    ;

    private Integer code;
    private String msgTyp;

    MsgTypeEnum(Integer code, String msgTyp) {
        this.code = code;
        this.msgTyp = msgTyp;
    }
}
