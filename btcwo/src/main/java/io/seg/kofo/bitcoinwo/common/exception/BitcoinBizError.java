package io.seg.kofo.bitcoinwo.common.exception;

import io.seg.kofo.common.exception.BizErrorCode;

public enum  BitcoinBizError implements BizErrorCode {

    //
    INVALIDE_PARAMETERS("001","invalide parameters"),
    //
    RPC_ERROR("002","rpc error"),

    //decode raw transaction error
    RAW_TRANSACTION_DECODE_ERROR("201","decode raw transaction error"),

    SEND_TRANSACTION_FAILED("202","send transaction failed"),

    ;

    private final String code;
    private final String description;

    private BitcoinBizError(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static BitcoinBizError getByCode(String code) {
        BitcoinBizError[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            BitcoinBizError scenario = var1[var3];
            if (scenario.getCode().equals(code)) {
                return scenario;
            }
        }

        return null;
    }
}
