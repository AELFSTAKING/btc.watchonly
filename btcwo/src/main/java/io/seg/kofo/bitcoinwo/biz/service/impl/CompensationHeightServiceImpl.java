

package io.seg.kofo.bitcoinwo.biz.service.impl;

import io.seg.framework.dao.BaseDao;
import io.seg.framework.dao.model.QueryResult;
import io.seg.kofo.bitcoinwo.biz.service.CompensationHeightService;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import io.seg.kofo.bitcoinwo.dao.po.CompensationHeightPo;


@Slf4j
@Service
public class CompensationHeightServiceImpl implements CompensationHeightService {
      @Autowired
      private BaseDao baseDao;

      @Override
      public CompensationHeightPo selectOne(CompensationHeightPo compensationHeight) {
       return baseDao.selectOne(compensationHeight);
      }

      @Override
      public QueryResult<CompensationHeightPo> selectLimit(CompensationHeightPo compensationHeight) {
       return baseDao.selectQueryResult(compensationHeight, compensationHeight.getPageIndex(), compensationHeight.getPageSize());
      }

      @Override
      public List<CompensationHeightPo> selectList(CompensationHeightPo compensationHeight) {
       return baseDao.selectList(compensationHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(CompensationHeightPo compensationHeight) {
       return baseDao.insert(compensationHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(CompensationHeightPo compensationHeight) {
       return baseDao.update(compensationHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(CompensationHeightPo set,CompensationHeightPo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(CompensationHeightPo compensationHeight) {
       return baseDao.delete(compensationHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<CompensationHeightPo> list) {
       return baseDao.batchInsert(CompensationHeightPo.class, list);
      }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSyncHeight(MsgQueuePo msgQueuePo){
        CompensationHeightPo syncHeightPo = baseDao.selectOne(CompensationHeightPo.builder().build());
        syncHeightPo.setSyncHeight(msgQueuePo.getHeight());
        syncHeightPo.setBlockHash(msgQueuePo.getBlockHash());
        syncHeightPo.setUpdateTime(new Date());
        boolean isUpdateSyncHeight = update(syncHeightPo) == 1;
    }

}
