package io.seg.kofo.bitcoinwo.biz.controller;

import com.alibaba.fastjson.JSON;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.config.WatchOnlyProperties;
import io.seg.kofo.bitcoinwo.common.exception.BitcoinBizError;
import io.seg.kofo.bitcoinwo.request.RawTransactionRequest;
import io.seg.kofo.bitcoinwo.response.BroadcastResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.bitcoinj.core.Utils.HEX;

@Slf4j
@RestController
@RequestMapping("/sendTransaction")
public class SendTransaction extends BaseController<RawTransactionRequest, String> {

    @Autowired
    private WatchOnlyProperties properties;
    @Autowired
    FullNodeCache fullNodeCache;

    @Override
    protected RespData<String> call(RawTransactionRequest rawTransactionRequest) {

        String rawTransaction = rawTransactionRequest.getRawTransaction();
        log.info("rawTransaction is {}", rawTransaction);
        Transaction transaction = null;
        try {
            transaction = new Transaction(properties.getParameters(), HEX.decode(rawTransaction));
            //标记为是我们发送的交易 便于计算余额
        } catch (Exception e) {
            log.error("decode raw transaction failed,{}", e.getMessage(), e);
            throw new BizException(BitcoinBizError.RAW_TRANSACTION_DECODE_ERROR, e.getMessage());
        }
        BroadcastResponse response;
        String resultString;
        try {
            resultString = fullNodeCache.getBitcoinClient().sendRawTransaction(rawTransaction);
            log.info("broadcast tx:{},rawTx:{} result:{}", transaction.getHashAsString(), rawTransaction, resultString);
        } catch (Exception e) {
            log.error("broadcast TX fail,{}", e.getMessage(), e);
            throw new BizException(BitcoinBizError.RPC_ERROR, e.getMessage());
        }
        //判断广播成功的标志 result会返回tx_hash
        if (transaction.getHashAsString().equals(resultString)) {
            response = broadcastResponse("BTC", "SUCCESS", transaction.getHashAsString());
        } else {
            //JsonRpcError error = JSON.parseObject(resultString,JsonRpcError.class);
            //String errorString = JSON.toJSONString(error);
            return RespData.error(BitcoinBizError.SEND_TRANSACTION_FAILED.toString(), "RPC  status != 200", resultString);
        }
        //fixme 返回对象string  之后应调整为统一的对象返回
        String responseString = JSON.toJSONString(response);
        return RespData.success(responseString);
    }

    public static BroadcastResponse broadcastResponse(String currencyName, String status, String txhash) {
        BroadcastResponse response = new BroadcastResponse();
        response.setCurrencyName(currencyName);
        //广播操作结果"SUCCESS"为广播成功
        response.setStatus(status);
        //广播操作成功时间
        response.setTimestamp(String.valueOf(System.currentTimeMillis()));
        response.setTxHash(txhash);

        return response;
    }
}
