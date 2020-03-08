package io.seg.kofo.bitcoinwo.biz.job;

import com.google.common.collect.Lists;

import io.seg.kofo.bitcoinwo.biz.service.BtcBlockHeightService;
import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.enums.MsgTypeEnum;
import com.alibaba.fastjson.JSON;

import io.seg.kofo.common.controller.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class MsgCallBackDataFlowJob {
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BtcBlockHeightService blockHeightService;


//    @Scheduled(initialDelay = 10000,fixedDelay = 10000)
    public void analyzeBlock() {
        int processCount = 0;

        log.info("analyze block start:");

        while (true) {
            MsgQueuePo msgQueuePo = fetchData();

            if (Objects.isNull(msgQueuePo)) {
                log.info("analyze block end,process {} block.", processCount);
                break;
            }
            processCount++;
            processData(msgQueuePo);
        }
    }

    public MsgQueuePo fetchData() {
        return  msgQueueService.getMsgForCallBack().get(0);
    }

    public void processData(MsgQueuePo msgQueuePo) {
        boolean isSuccess = false;
        //todo callback biz msgQueuePo.getMsg()
        log.info("processData callbackBlockInfo requestHeight:{} ",msgQueuePo.getHeight());
        if (isSuccess){
            //更新msg回调状态 记录最新回调高度
            msgQueueService.updateCallBackInfo(msgQueuePo);
        }

    }
}
