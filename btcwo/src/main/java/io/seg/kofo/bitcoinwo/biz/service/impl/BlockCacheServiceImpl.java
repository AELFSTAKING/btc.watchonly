

package io.seg.kofo.bitcoinwo.biz.service.impl;

import io.seg.framework.dao.IBaseDao;
import io.seg.kofo.bitcoinwo.biz.service.BlockCacheService;
import io.seg.kofo.bitcoinwo.biz.service.SyncHeightService;
import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.SyncHeightPo;
import io.seg.framework.dao.model.QueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class BlockCacheServiceImpl implements BlockCacheService {
      @Autowired
      private IBaseDao baseDao;
      @Autowired
      private SyncHeightService syncHeightService;

      @Override
      public BlockCachePo selectOne(BlockCachePo blockCache) {
       return baseDao.selectOne(blockCache);
      }

      @Override
      public QueryResult<BlockCachePo> selectLimit(BlockCachePo blockCache) {
       return baseDao.selectQueryResult(blockCache, blockCache.getPageIndex(), blockCache.getPageSize());
      }

      @Override
      public List<BlockCachePo> selectList(BlockCachePo blockCache) {
       return baseDao.selectList(blockCache);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int insert(BlockCachePo blockCache) {
       return baseDao.insert(blockCache);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BlockCachePo blockCache) {
       return baseDao.update(blockCache);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public int update(BlockCachePo set,BlockCachePo where){
       return baseDao.update(set,where);
      }


      @Override
      @Transactional(rollbackFor = Exception.class)
      public int delete(BlockCachePo blockCache) {
       return baseDao.delete(blockCache);
      }

      @Override
      @Transactional(rollbackFor = Exception.class)
      public boolean batchInsert(List<BlockCachePo> list) {
       return baseDao.batchInsert(BlockCachePo.class, list);
      }


    @Override
    public boolean cleanBlockCacheBySyncHeight(Integer distance){
        SyncHeightPo syncHeightPo = syncHeightService.selectOne(SyncHeightPo.builder().build());
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("before", String.valueOf(syncHeightPo.getSyncHeight()-distance));
        paramMap.put("after", String.valueOf(syncHeightPo.getSyncHeight()));
        //删除指定距离之前的 以及当前指定sync_height之后的
        return this.baseDao.delete("delete_distance", paramMap) >= 0;
    }


}
