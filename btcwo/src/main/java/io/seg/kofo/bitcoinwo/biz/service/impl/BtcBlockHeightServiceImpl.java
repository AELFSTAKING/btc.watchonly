

package io.seg.kofo.bitcoinwo.biz.service.impl;

import io.seg.kofo.bitcoinwo.biz.service.BtcBlockHeightService;
import io.seg.kofo.bitcoinwo.dao.po.BtcBlockHeightPo;
import io.seg.framework.dao.BaseDao;
import io.seg.framework.dao.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Slf4j
@Service
public class BtcBlockHeightServiceImpl implements BtcBlockHeightService {
      @Autowired
      private BaseDao baseDao;

      @Override
      public BtcBlockHeightPo selectOne(BtcBlockHeightPo btcBlockHeight) {
       return baseDao.selectOne(btcBlockHeight);
      }

      @Override
      public QueryResult<BtcBlockHeightPo> selectLimit(BtcBlockHeightPo btcBlockHeight) {
       return baseDao.selectQueryResult(btcBlockHeight, btcBlockHeight.getPageIndex(), btcBlockHeight.getPageSize());
      }

      @Override
      public List<BtcBlockHeightPo> selectList(BtcBlockHeightPo btcBlockHeight) {
       return baseDao.selectList(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(BtcBlockHeightPo btcBlockHeight) {
       return baseDao.insert(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BtcBlockHeightPo btcBlockHeight) {
       return baseDao.update(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BtcBlockHeightPo set,BtcBlockHeightPo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(BtcBlockHeightPo btcBlockHeight) {
       return baseDao.delete(btcBlockHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<BtcBlockHeightPo> list) {
       return baseDao.batchInsert(BtcBlockHeightPo.class, list);
      }

}
