
package io.seg.kofo.bitcoinwo.biz.service;

import io.seg.framework.dao.model.QueryResult;
import io.seg.kofo.bitcoinwo.dao.po.BlockMsgPo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;

import java.util.List;

public interface BlockMsgService {

     /**
      * 根据唯一索引查询
      * @param blockMsg
      * @return
      */
     public BlockMsgPo selectOne(BlockMsgPo blockMsg);

     /**
      * 分页查询
      * @param blockMsg
      * @return
      */
     public QueryResult<BlockMsgPo> selectLimit(BlockMsgPo blockMsg);

     /**
      * 列表查询不分页
      * @param blockMsg
      * @return
      */
     public List<BlockMsgPo> selectList(BlockMsgPo blockMsg);

     /**
      * 新增
      * @param blockMsg
      * @return
      */
     public int insert(BlockMsgPo blockMsg);

     /**
      * 更新
      * @param blockMsg
      * @return
      */
     public int update(BlockMsgPo blockMsg);

     /**
      * 更新
      * @param set
      * @param where
      * @return
      */
     public int update(BlockMsgPo set, BlockMsgPo where);

     /**
      * 删除
      * @param blockMsg
      * @return
      */
     public int delete(BlockMsgPo blockMsg);

     /**
      * 批量插入
      * @param list
      * @return
      */
     public boolean batchInsert(List<BlockMsgPo> list);

     public boolean saveBlockMsg(MsgQueuePo msgQueuePo);

}
