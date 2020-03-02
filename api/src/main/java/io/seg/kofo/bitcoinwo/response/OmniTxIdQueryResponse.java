package io.seg.kofo.bitcoinwo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class OmniTxIdQueryResponse {
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

    public OmniTxIdQueryResponse(String fee, String referenceAddress, String sendingAddress, String txid, String type, int typeInt, boolean valid, String invalidReason, int version, Map originJson) {
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
