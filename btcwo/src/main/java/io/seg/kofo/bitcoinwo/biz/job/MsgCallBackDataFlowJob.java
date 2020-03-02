package io.seg.kofo.bitcoinwo.biz.job;

import io.seg.kofo.bitcoin.analyzer.BO.BlockInfo;
import io.seg.kofo.bitcoin.analyzer.api.BitcoinAnalyzerClient;
import io.seg.kofo.bitcoin.analyzer.req.CallbackBlockInfoReq;
import io.seg.kofo.bitcoinwo.biz.service.BtcBlockHeightService;
import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.enums.MsgTypeEnum;
import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import io.seg.elasticjob.common.collect.Lists;

import io.seg.kofo.common.controller.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MsgCallBackDataFlowJob implements DataflowJob{
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BtcBlockHeightService blockHeightService;
    @Autowired
    BitcoinAnalyzerClient bitcoinAnalyzerClient;



    @Override
    public List fetchData(ShardingContext shardingContext) {
        return  msgQueueService.getMsgForCallBack();
    }

    @Override
    public void processData(ShardingContext shardingContext, List list) {
        MsgQueuePo msgQueuePo = (MsgQueuePo) list.get(0);
        BlockInfo blockInfo = null;
        if (MsgTypeEnum.BLOCK_MSG.getCode().intValue() == msgQueuePo.getMsgType()){
            blockInfo = JSON.parseObject(msgQueuePo.getMsg(), BlockInfo.class);
        }
        CallbackBlockInfoReq req = CallbackBlockInfoReq.builder()
                .type(msgQueuePo.getMsgType())
                .data(Lists.newArrayList(blockInfo))
                .height(Math.toIntExact(msgQueuePo.getHeight()))
                .build();
        //feign调用
        RespData<String> respData = bitcoinAnalyzerClient.callbackBlockInfo(req);
        log.info("processData callbackBlockInfo requestHeight:{} response:{}",msgQueuePo.getHeight(),respData);
        if (respData.isSuccess()){
            //更新msg回调状态 记录最新回调高度
            msgQueueService.updateCallBackInfo(msgQueuePo);
        }

    }
}
