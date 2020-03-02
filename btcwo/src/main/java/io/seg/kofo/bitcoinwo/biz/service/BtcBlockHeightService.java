
package io.seg.kofo.bitcoinwo.biz.service;

import io.seg.kofo.bitcoinwo.dao.po.BtcBlockHeightPo;
import io.seg.framework.dao.model.QueryResult;

import java.util.List;

public interface BtcBlockHeightService {

     /**
      * 根据唯一索引查询
      * @param btcBlockHeight
      * @return
      */
     public BtcBlockHeightPo selectOne(BtcBlockHeightPo btcBlockHeight);

     /**
      * 分页查询
      * @param btcBlockHeight
      * @return
      */
     public QueryResult<BtcBlockHeightPo> selectLimit(BtcBlockHeightPo btcBlockHeight);

     /**
      * 列表查询不分页
      * @param btcBlockHeight
      * @return
      */
     public List<BtcBlockHeightPo> selectList(BtcBlockHeightPo btcBlockHeight);

     /**
      * 新增
      * @param btcBlockHeight
      * @return
      */
     public int insert(BtcBlockHeightPo btcBlockHeight);

     /**
      * 更新
      * @param btcBlockHeight
      * @return
      */
     public int update(BtcBlockHeightPo btcBlockHeight);

     /**
      * 更新
      * @param set
      * @param where
      * @return
      */
     public int update(BtcBlockHeightPo set, BtcBlockHeightPo where);

     /**
      * 删除
      * @param btcBlockHeight
      * @return
      */
     public int delete(BtcBlockHeightPo btcBlockHeight);

     /**
      * 批量插入
      * @param list
      * @return
      */
     public boolean batchInsert(List<BtcBlockHeightPo> list);

}
