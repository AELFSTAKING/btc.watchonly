package io.seg.kofo.bitcoinwo.biz.controller;

import io.seg.kofo.bitcoinwo.biz.service.BlockMsgService;
import io.seg.kofo.bitcoinwo.common.exception.BitcoinBizError;
import io.seg.kofo.bitcoinwo.dao.po.BlockMsgPo;
import io.seg.kofo.bitcoinwo.request.QueryBlockRequest;
import io.seg.kofo.bitcoinwo.response.BlockMsgResponse;
import io.seg.kofo.common.controller.BaseController;
import io.seg.kofo.common.controller.RespData;
import io.seg.kofo.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/queryBlockMsg")
public class QueryBlock extends BaseController<QueryBlockRequest, BlockMsgResponse> {

    @Autowired
    BlockMsgService blockMsgService;

    @Override
    protected RespData<BlockMsgResponse> call(QueryBlockRequest queryBlockRequest) {

        BlockMsgResponse response = null;
        try {
            BlockMsgPo blockMsgPo = blockMsgService.selectOne(BlockMsgPo.builder()
                    .height(queryBlockRequest.getHeight())
                    .build());
            if (Objects.nonNull(blockMsgPo)){
                response = BlockMsgResponse.builder()
                        .msg(blockMsgPo.getMsg())
                        .blockHash(blockMsgPo.getHash())
                        .height(blockMsgPo.getHeight())
                        .build();
                log.info("queryBlock resp height:{},hash:{}",response.getHeight(),response.getBlockHash());
            }

        } catch (Exception e) {
            throw new BizException(BitcoinBizError.RPC_ERROR,e);
        }
        return RespData.success(response);
    }

}
