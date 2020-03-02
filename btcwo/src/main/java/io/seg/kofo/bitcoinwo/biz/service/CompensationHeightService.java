
package io.seg.kofo.bitcoinwo.biz.service;

import io.seg.framework.dao.model.QueryResult;
import io.seg.kofo.bitcoinwo.dao.po.CompensationHeightPo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;

import java.util.List;

public interface CompensationHeightService {

     /**
      * 根据唯一索引查询
      * @param compensationHeight
      * @return
      */
     public CompensationHeightPo selectOne(CompensationHeightPo compensationHeight);

     /**
      * 分页查询
      * @param compensationHeight
      * @return
      */
     public QueryResult<CompensationHeightPo> selectLimit(CompensationHeightPo compensationHeight);

     /**
      * 列表查询不分页
      * @param compensationHeight
      * @return
      */
     public List<CompensationHeightPo> selectList(CompensationHeightPo compensationHeight);

     /**
      * 新增
      * @param compensationHeight
      * @return
      */
     public int insert(CompensationHeightPo compensationHeight);

     /**
      * 更新
      * @param compensationHeight
      * @return
      */
     public int update(CompensationHeightPo compensationHeight);

     /**
      * 更新
      * @param set
      * @param where
      * @return
      */
     public int update(CompensationHeightPo set, CompensationHeightPo where);

     /**
      * 删除
      * @param compensationHeight
      * @return
      */
     public int delete(CompensationHeightPo compensationHeight);

     /**
      * 批量插入
      * @param list
      * @return
      */
     public boolean batchInsert(List<CompensationHeightPo> list);

     public void updateSyncHeight(MsgQueuePo msgQueuePo);

}
