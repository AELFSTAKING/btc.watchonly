package io.seg.kofo.bitcoinwo.biz.controller;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.exception.BitcoinBizError;
import io.seg.kofo.bitcoinwo.request.QueryIdRequest;
import io.seg.kofo.bitcoinwo.response.TxIdQueryResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/queryId")
public class QueryId extends BaseController<QueryIdRequest, TxIdQueryResponse> {

    @Autowired
    FullNodeCache fullNodeCache;

    @Override
    protected RespData<TxIdQueryResponse> call(QueryIdRequest queryIdRequest) {

        TxIdQueryResponse response = new TxIdQueryResponse();
        try {
            Bitcoin.RawTransaction rawTransaction= fullNodeCache.getBitcoinClient().getRawTransaction(queryIdRequest.getQueryId());
            int height = fullNodeCache.getBitcoinClient().getBlockCount() - rawTransaction.confirmations() + 1;
            response.setBlockHash(rawTransaction.blockHash());
            response.setBlockHeight(height);
            response.setConfirmations(rawTransaction.confirmations());
            response.setRawTransaction(rawTransaction.hex());
        } catch (Exception e) {
            throw new BizException(BitcoinBizError.RPC_ERROR,e);
        }
        log.info("queryId resp:{}",response);
        //todo 此处错误码也会通过string透传到调用方 ，之后应该考虑用对象的方式而不是错误对象字符串
        return RespData.success(response);
    }

}
