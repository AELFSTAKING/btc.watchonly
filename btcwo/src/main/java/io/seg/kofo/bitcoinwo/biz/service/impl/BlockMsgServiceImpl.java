

package io.seg.kofo.bitcoinwo.biz.service.impl;

import io.seg.framework.dao.BaseDao;
import io.seg.framework.dao.model.QueryResult;
import io.seg.kofo.bitcoinwo.biz.service.BlockMsgService;
import io.seg.kofo.bitcoinwo.biz.service.SyncHeightService;
import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.dao.po.SyncHeightPo;
import io.seg.kofo.bitcoinwo.enums.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import io.seg.kofo.bitcoinwo.dao.po.BlockMsgPo;


@Slf4j
@Service
public class BlockMsgServiceImpl implements BlockMsgService {
      @Autowired
      private BaseDao baseDao;

      @Autowired
      private SyncHeightService syncHeightService;

      @Override
      public BlockMsgPo selectOne(BlockMsgPo blockMsg) {
       return baseDao.selectOne(blockMsg);
      }

      @Override
      public QueryResult<BlockMsgPo> selectLimit(BlockMsgPo blockMsg) {
       return baseDao.selectQueryResult(blockMsg, blockMsg.getPageIndex(), blockMsg.getPageSize());
      }

      @Override
      public List<BlockMsgPo> selectList(BlockMsgPo blockMsg) {
       return baseDao.selectList(blockMsg);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(BlockMsgPo blockMsg) {
       return baseDao.insert(blockMsg);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BlockMsgPo blockMsg) {
       return baseDao.update(blockMsg);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BlockMsgPo set,BlockMsgPo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(BlockMsgPo blockMsg) {
       return baseDao.delete(blockMsg);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<BlockMsgPo> list) {
       return baseDao.batchInsert(BlockMsgPo.class, list);
      }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBlockMsg(MsgQueuePo msgQueuePo) {
        if (msgQueuePo.getMsgType().equals(MsgTypeEnum.FORKING_MSG.getCode())){
            //分叉处理
            //删除blockMsg中缓存记录(fork_height之后的)
            int count = 0;
            SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
            long preSyncHeight = syncHeightPo.getSyncHeight();
            for (int i = Math.toIntExact(msgQueuePo.getHeight()); i < preSyncHeight; i++) {
                count += baseDao.delete(BlockMsgPo.builder()
                        .height((long) (i + 1))
                        .build());
            }
            boolean isDeleteBlockMsg = count == preSyncHeight - msgQueuePo.getHeight();
            if (!isDeleteBlockMsg) {
                throw new RuntimeException("isDeleteBlockCache generateForkingMsg Exception");
            }
        }

        /**
         * 将cache的部分换成blockMsg即可。
         */
        List<BlockMsgPo> cachedBlock = selectLimit(BlockMsgPo.builder()
                .hash(msgQueuePo.getBlockHash())
                .pageIndex(1)
                .pageSize(1)
                .build()).getRows();
        boolean isInsertBlockMsg = true;
        if (cachedBlock.size() <= 0) {
            BlockMsgPo blockCachePo = BlockMsgPo.builder()
                    .height(msgQueuePo.getHeight())
                    .hash(msgQueuePo.getBlockHash())
                    .msg(msgQueuePo.getMsg())
                    .msgType(MsgTypeEnum.BLOCK_MSG.getCode())
                    .build();
            //不存在该hash则插入
            isInsertBlockMsg = insert(blockCachePo) == 1;
        }
        if (isInsertBlockMsg   ) {
            return true;
        } else {
            throw new RuntimeException("saveBlockMsg exception");
        }
    }
}
