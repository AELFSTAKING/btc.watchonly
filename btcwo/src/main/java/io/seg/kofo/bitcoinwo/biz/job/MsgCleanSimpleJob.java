package io.seg.kofo.bitcoinwo.biz.job;

import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MsgCleanSimpleJob{
    @Autowired
    MsgQueueService msgQueueService;

    @Scheduled(initialDelay = 20000,fixedDelay = 10000)
    public void execute() {
        //删除已经回调成功的消息
        msgQueueService.delete(MsgQueuePo.builder()
                .isCallback(true)
                .build());
        //todo 删除距离sync_height过远的区块缓存？ 距离>10000？
    }
}
