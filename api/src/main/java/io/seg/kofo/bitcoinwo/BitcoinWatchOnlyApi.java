package io.seg.kofo.bitcoinwo;

import io.seg.kofo.bitcoinwo.request.OmniBalanceRequest;
import io.seg.kofo.bitcoinwo.request.QueryBlockRequest;
import io.seg.kofo.bitcoinwo.request.QueryIdRequest;
import io.seg.kofo.bitcoinwo.request.RawTransactionRequest;
import io.seg.kofo.bitcoinwo.response.BlockMsgResponse;
import io.seg.kofo.bitcoinwo.response.OmniBalanceResponse;
import io.seg.kofo.bitcoinwo.response.TxIdQueryResponse;
import io.seg.kofo.common.controller.RespData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bitcoin-wo")
public interface BitcoinWatchOnlyApi {

    /**
     * 获取最新的高度，这个高度是通过各种方式获取到最新的区块高度，不仅仅是查询自己的全节点
     *
     * @return
     */
    @GetMapping("/lastestHeight")
    RespData<Integer> lastestHeight();

    /**
     * 根据全节点返回值
     * @return
     */
    @GetMapping("/estimateFee")
    RespData<Long> estimateFee(@RequestParam("confirmTarget") Integer confirmTarget);

    /**
     * 发送交易
     *
     * @param rawTransaction
     * @return
     */
    @PostMapping("/sendTransaction")
    RespData<String> sendTransaction(@RequestBody RawTransactionRequest rawTransaction);

    /**
     * 获取btc交易
     *
     * @param queryId
     * @return
     */
    @PostMapping("/queryId")
    RespData<TxIdQueryResponse> queryId(@RequestBody QueryIdRequest queryId);

    /**
     * 获取omni余额
     *
     * @param queryBalance
     * @return
     */
    @PostMapping("/queryOmniBalance")
    RespData<OmniBalanceResponse> queryOmniBalance(@RequestBody OmniBalanceRequest queryBalance);

    /**
     * 获取区块信息
     *
     * @param queryBlockRequest
     * @return
     */
    @PostMapping("/queryBlockMsg")
    RespData<BlockMsgResponse> queryBlockMsg(@RequestBody QueryBlockRequest queryBlockRequest);


}
