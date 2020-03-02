package io.seg.kofo.bitcoinwo.biz.controller;

import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.common.config.WatchOnlyProperties;
import io.seg.kofo.bitcoinwo.common.exception.BitcoinBizError;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import io.seg.kofo.common.exception.KofoCommonBizError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/lastestHeight")
public class QueryHeightWeb {

    @Autowired
    FullNodeCache fullNodeCache;

    @RequestMapping({""})
    @ResponseBody
    protected RespData<Integer> call() {
        Integer height;
        try {
            height = fullNodeCache.getBitcoinClient().getBlockCount();
            return RespData.success(height);
        } catch (Exception e) {
            log.error("query local height failed : {}", e.getMessage(),e);
            return RespData.error(BitcoinBizError.RPC_ERROR.getCode(),e.getMessage());
        }
    }
}

