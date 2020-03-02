package io.seg.kofo.bitcoinwo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OmniBalanceResponse {
    private String balance;
    private String reserved;
    private String frozen;

    @Builder
    public OmniBalanceResponse(String balance, String reserved, String frozen) {
        this.balance = balance;
        this.reserved = reserved;
        this.frozen = frozen;
    }
}
