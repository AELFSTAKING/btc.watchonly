package io.seg.kofo.bitcoinwo.biz.controller;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.exception.BitcoinBizError;
import io.seg.kofo.bitcoinwo.request.OmniBalanceRequest;
import io.seg.kofo.bitcoinwo.response.OmniBalanceResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/queryOmniBalance")
public class QueryOmniBalance extends BaseController<OmniBalanceRequest, OmniBalanceResponse> {

    @Autowired
    FullNodeCache fullNodeCache;

    @Override
    protected RespData<OmniBalanceResponse> call(OmniBalanceRequest omniBalanceRequest) {

        Bitcoin.OmniBalance omniBalance = null;
        try {
            omniBalance = fullNodeCache.getOmniClient().omniGetBalance(omniBalanceRequest.getAddress(), omniBalanceRequest.getPropertyId());
        } catch (Exception e) {
            throw new BizException(BitcoinBizError.RPC_ERROR,e);
        }
        log.info("queryOmniBalance resp:{}",omniBalance.toString());
        return RespData.success(OmniBalanceResponse.builder()
                .balance(omniBalance.balance())
                .reserved(omniBalance.reserved())
                .frozen(omniBalance.frozen())
                .build());
    }

}
