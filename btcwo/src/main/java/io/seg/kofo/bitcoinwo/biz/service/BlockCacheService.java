
package io.seg.kofo.bitcoinwo.biz.service;

import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.framework.dao.model.QueryResult;

import java.util.List;

public interface BlockCacheService {

    /**
     * 根据唯一索引查询
     *
     * @param blockCache
     * @return
     */
    public BlockCachePo selectOne(BlockCachePo blockCache);

    /**
     * 分页查询
     *
     * @param blockCache
     * @return
     */
    public QueryResult<BlockCachePo> selectLimit(BlockCachePo blockCache);

    /**
     * 列表查询不分页
     *
     * @param blockCache
     * @return
     */
    public List<BlockCachePo> selectList(BlockCachePo blockCache);

    /**
     * 新增
     *
     * @param blockCache
     * @return
     */
    public int insert(BlockCachePo blockCache);

    /**
     * 更新
     *
     * @param blockCache
     * @return
     */
    public int update(BlockCachePo blockCache);

    /**
     * 更新
     *
     * @param set
     * @param where
     * @return
     */
    public int update(BlockCachePo set, BlockCachePo where);

    /**
     * 删除
     *
     * @param blockCache
     * @return
     */
    public int delete(BlockCachePo blockCache);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public boolean batchInsert(List<BlockCachePo> list);

    /**
     * 删除当前同步高度指定距离之前的
     * 以及当前同步高度之后的区块缓存
     *
     * @param distance
     * @return
     */
    public boolean cleanBlockCacheBySyncHeight(Integer distance);
}
