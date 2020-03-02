package io.seg.kofo.bitcoinwo.model.bo;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BlockJob {
    private Bitcoin.Block block;
    boolean isFork;
}
