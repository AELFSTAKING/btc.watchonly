package io.seg.kofo.bitcoinwo.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public  class OmniBalanceRequest {
    private String address;
    private long propertyId;


    @Builder
    public OmniBalanceRequest(String address, long propertyId) {
        this.address = address;
        this.propertyId = propertyId;
    }
}