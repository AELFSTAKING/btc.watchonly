package io.seg.kofo.bitcoinwo.model.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yangjiyun
 * @Description:
 * @date 2018/11/22 下午9:28
 */
@Getter
@Setter
@NoArgsConstructor
public class OmniTx implements Serializable {
    private static final long serialVersionUID = 1L;
    private long height;
    private String blockHash;
    private long blockTime;
    private long positionInBlock;
    private String fee;
    private String referenceAddress;
    private String sendingAddress;
    private String txid;
    private String type;
    private int typeInt;
    private boolean valid;
    private String invalidReason;
    private int version;
    private Map originJson;

    @Builder
    public OmniTx(long height, String blockHash, long blockTime, long positionInBlock, String fee, String referenceAddress, String sendingAddress, String txid, String type, int typeInt, boolean valid, String invalidReason, int version, Map originJson) {
        this.height = height;
        this.blockHash = blockHash;
        this.blockTime = blockTime;
        this.positionInBlock = positionInBlock;
        this.fee = fee;
        this.referenceAddress = referenceAddress;
        this.sendingAddress = sendingAddress;
        this.txid = txid;
        this.type = type;
        this.typeInt = typeInt;
        this.valid = valid;
        this.invalidReason = invalidReason;
        this.version = version;
        this.originJson = originJson;
    }
}
