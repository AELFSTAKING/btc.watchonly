import io.seg.kofo.bitcoin.analyzer.api.BitcoinAnalyzerClient;
import io.seg.kofo.bitcoin.analyzer.req.CallbackBlockHeightReq;
import io.seg.kofo.bitcoinwo.BtcWoApplication;
import io.seg.kofo.bitcoinwo.biz.service.MsgQueueService;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.framework.sequence.sdk.produce.SequenceProduce;
import io.seg.kofo.common.controller.RespData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BtcWoApplication.class)
@Slf4j
public class MsgQueueServiceTest {
    @Autowired
    MsgQueueService msgQueueService;
    @Autowired
    SequenceProduce sequenceProduce;
    @Autowired
    BitcoinAnalyzerClient bitcoinAnalyzerClient;


    /**
     * 事务测试
     */
    @Test
    public void testTx() {

        Date time = new Date();
        for (int i = 0 ; i < 1000 ; i ++){
            String sequence = sequenceProduce.getSequence("testname");
            MsgQueuePo msgQueuePo = MsgQueuePo.builder().msg(sequence).msgType(0).createTime(DateUtils.addHours(time,1)).build();
            msgQueueService.insert(msgQueuePo);
            log.info("Insert:{}",msgQueuePo);
        }
    }

    @Test
    public void contextLoads2() {
        CallbackBlockHeightReq blockInfoReq = new CallbackBlockHeightReq();
        blockInfoReq.setLatestBlockHeight(10);
        blockInfoReq.setNodeLatestBlockHeight(20);
        RespData respData =bitcoinAnalyzerClient.callbackBlockHeight(blockInfoReq);
    }

    @Test
    public void contextLoads3() {
        CallbackBlockHeightReq blockInfoReq = new CallbackBlockHeightReq();
        blockInfoReq.setLatestBlockHeight(10);
        blockInfoReq.setNodeLatestBlockHeight(20);
//        RespData respData =watchOnlyController.lastestHeight();
//        System.out.println(respData);
    }
}