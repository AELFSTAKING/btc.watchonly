package io.seg.kofo.bitcoinwo.biz.job;

import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MsgCleanSimpleJob implements SimpleJob {
    @Autowired
    MsgQueueService msgQueueService;

    @Override
    public void execute(ShardingContext shardingContext) {
        //删除已经回调成功的消息
        msgQueueService.delete(MsgQueuePo.builder()
                .isCallback(true)
                .build());
        //todo 删除距离sync_height过远的区块缓存？ 距离>10000？
    }
}
