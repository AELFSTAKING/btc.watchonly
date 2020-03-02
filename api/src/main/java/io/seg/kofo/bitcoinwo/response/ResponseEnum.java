package io.seg.kofo.bitcoinwo.response;

/**
 * @author gin
 */

public enum ResponseEnum {


    SUCCESS("1000", "0001", "SUCCESS"),

    /**
     * 广播失败类错误
     */
    BROADCAST_FAILED("4000","0100","Broadcast failed."),
    BROADCAST_FAILED_TX_ILLEGAL("4000","0101","Broadcast failed for tx illegal."),
    BROADCAST_FAILED_BITCOIND_FAILED("4000","0102","Broadcast failed for bitcoind rpc failed."),
    /**
     *
     */
    SYS_ERROR("4000", "0000", "System Error."),
    INVALIDE_PARAMETERS("4000", "0001", "Invalid Parameters."),
    RPC_FAILED("4000","0200","Rpc failed.");




    private final String mainCode;
    private final String subCode;
    private final String desc;


    ResponseEnum(String mainCode, String subCode, String desc) {
        this.mainCode = mainCode;
        this.subCode = subCode;
        this.desc = desc;
    }


    public String getMainCode() {
        return mainCode;
    }

    public String getDesc() {
        return desc;
    }

    public String getSubCode() {
        return subCode;
    }
}
