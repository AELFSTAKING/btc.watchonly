package io.seg.kofo.bitcoinwo.biz.controller;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.exception.BitcoinBizError;
import io.seg.kofo.common.controller.RespData;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Coin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/estimateFee")
public class EstimateFee {

    @Autowired
    FullNodeCache fullNodeCache;

    @RequestMapping({""})
    @ResponseBody
    protected RespData<Long> call(Integer confirmTarget) {
        confirmTarget = confirmTarget == 0 ? 1:confirmTarget;
        log.info("estimateFee confirm target:{}",confirmTarget);
        Coin feePrice = Coin.valueOf(20000L);
        try {
            /**
             * rpc调用查到的费率是以btc为单位的 btc/kb
             */
            Bitcoin.SmartFee smartFee = fullNodeCache.getBitcoinClient().estimateSmartFee(confirmTarget);
            if (Objects.nonNull(smartFee) && Objects.nonNull(smartFee.feeRate())){
                feePrice = Coin.parseCoin(smartFee.feeRate());
            }
        } catch (Exception e) {
            log.error("estimateSmartFee exception :{}",e.getMessage(),e);
        }
        //上限220000 并固定到180000
        if (feePrice.compareTo(Coin.valueOf(200000L)) > 0 ){
            feePrice = Coin.valueOf(180000L);
        }
        //下限10000 固定到15000
        if (feePrice.compareTo(Coin.valueOf(10000L) )<=0){
            feePrice = Coin.valueOf(15000L);
        }
        return RespData.success(feePrice.value);
    }
}

