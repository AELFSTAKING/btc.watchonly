package io.seg.kofo.bitcoinwo.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsonRpcError {
    String code;
    String message;
}
