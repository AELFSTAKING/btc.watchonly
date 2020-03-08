package io.seg.kofo.bitcoinwo.biz.controller;

import com.alibaba.fastjson.JSON;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.google.common.collect.Lists;
import io.seg.kofo.bitcoinwo.biz.service.BlockMsgService;
import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.common.config.FullNodeCache;
import io.seg.kofo.bitcoinwo.dao.po.BlockMsgPo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.enums.MsgTypeEnum;
import io.seg.kofo.bitcoinwo.model.bo.BlockInfo;
import io.seg.kofo.common.controller.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/replayAtHeight")
public class Replay {

    @Autowired
    FullNodeCache fullNodeCache;
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    BlockMsgService blockMsgService;


    @RequestMapping({""})
    @ResponseBody
    protected RespData<Boolean> call(Long height) {
        log.info("replay height :{}",height);
        boolean isSuccess = false;
        try {
            BlockMsgPo blockMsgPo = blockMsgService.selectOne(BlockMsgPo.builder()
                    .height(height).build());
            if (Objects.isNull(blockMsgPo)){
                //db没存储该区块 则从全节点获取
                Bitcoin bitcoin = fullNodeCache.getBitcoinClient();
                Bitcoin.BlockHeader header = bitcoin.getBlockHeaderAtHeight(Math.toIntExact(height));
                Bitcoin.Block syncBlock = fullNodeCache.getBitcoinClient().getBlock(header.hash());
                log.info("syncHeight blockhash:{},height:{}", syncBlock.hash(), syncBlock.height());

                MsgQueuePo msgQueuePo = msgQueueService.generateBlockMsg(syncBlock);
                isSuccess = msgQueueService.saveBlockInfo(msgQueuePo);
                /**
                 * 保存msg信息 以便之后gateway直接从wo的db中读取区块 ，充当全节点的proxy
                 */
                blockMsgService.saveBlockMsg(msgQueuePo);
                blockMsgPo = BlockMsgPo.builder()
                        .height(msgQueuePo.getHeight())
                        .hash(msgQueuePo.getBlockHash())
                        .msg(msgQueuePo.getMsg())
                        .msgType(MsgTypeEnum.BLOCK_MSG.getCode())
                        .build();
            }
            BlockInfo blockInfo = JSON.parseObject(blockMsgPo.getMsg(), BlockInfo.class);
            boolean isInvokeSuccess = false;
            //todo  invoke biz api blockInfo
            if (isInvokeSuccess){
                isSuccess = true;
            }

        } catch (Exception e) {
            log.error("replay height exception :{}",e.getMessage(),e);
        }
        log.info("replay height :{} result:{}",height,isSuccess);
        return RespData.success(isSuccess);
    }
}

