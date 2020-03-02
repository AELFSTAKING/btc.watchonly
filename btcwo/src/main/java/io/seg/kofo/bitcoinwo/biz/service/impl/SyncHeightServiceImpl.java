

package io.seg.kofo.bitcoinwo.biz.service.impl;

import io.seg.kofo.bitcoinwo.biz.service.BlockCacheService;
import io.seg.kofo.bitcoinwo.biz.service.SyncHeightService;
import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.dao.po.SyncHeightPo;
import io.seg.framework.dao.BaseDao;
import io.seg.framework.dao.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SyncHeightServiceImpl implements SyncHeightService {
      @Autowired
      private BaseDao baseDao;
      @Autowired
      private BlockCacheService blockCacheService;

      @Override
      public SyncHeightPo selectOne(SyncHeightPo syncHeight) {
       return baseDao.selectOne(syncHeight);
      }

      @Override
      public QueryResult<SyncHeightPo> selectLimit(SyncHeightPo syncHeight) {
       return baseDao.selectQueryResult(syncHeight, syncHeight.getPageIndex(), syncHeight.getPageSize());
      }

      @Override
      public List<SyncHeightPo> selectList(SyncHeightPo syncHeight) {
       return baseDao.selectList(syncHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(SyncHeightPo syncHeight) {
       return baseDao.insert(syncHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(SyncHeightPo syncHeight) {
       return baseDao.update(syncHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(SyncHeightPo set,SyncHeightPo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(SyncHeightPo syncHeight) {
       return baseDao.delete(syncHeight);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<SyncHeightPo> list) {
       return baseDao.batchInsert(SyncHeightPo.class, list);
      }

        @Override
        @Transactional(rollbackFor = Exception.class)
      public void updateSyncHeight(MsgQueuePo msgQueuePo){
          SyncHeightPo syncHeightPo = baseDao.selectOne(SyncHeightPo.builder().build());
          syncHeightPo.setSyncHeight(msgQueuePo.getHeight());
          syncHeightPo.setBlockHash(msgQueuePo.getBlockHash());
          syncHeightPo.setUpdateTime(new Date());
          boolean isUpdateSyncHeight = update(syncHeightPo) == 1;
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean setSyncHeightTo(Integer height, BlockCachePo targetBlockCache){
          //更新同步高度到指定值
          SyncHeightPo syncHeightPo = this.baseDao.selectOne(SyncHeightPo.builder().build());
          syncHeightPo.setSyncHeight(Long.valueOf(height));
          syncHeightPo.setUpdateTime(new Date());
          this.baseDao.update(syncHeightPo);

          BlockCachePo blockCachePo = blockCacheService.selectList(BlockCachePo.builder()
                  .blockHash(targetBlockCache.getBlockHash())
                  .build()).get(0);
          if (null == blockCachePo){
              blockCachePo = BlockCachePo.builder()
                      .blockHash(targetBlockCache.getBlockHash())
                      .height(targetBlockCache.getHeight())
                      .blockTime(targetBlockCache.getBlockTime())
                      .build();
              blockCacheService.insert(blockCachePo);
          }
          //保留100个前置区块缓存
          blockCacheService.cleanBlockCacheBySyncHeight(10);

          return true;

      }

}
