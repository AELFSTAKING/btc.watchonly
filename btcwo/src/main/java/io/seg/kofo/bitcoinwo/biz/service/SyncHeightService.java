
package io.seg.kofo.bitcoinwo.biz.service;

import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import io.seg.kofo.bitcoinwo.dao.po.SyncHeightPo;
import io.seg.framework.dao.model.QueryResult;

import java.util.List;

public interface SyncHeightService {

    /**
     * 根据唯一索引查询
     *
     * @param syncHeight
     * @return
     */
    public SyncHeightPo selectOne(SyncHeightPo syncHeight);

    /**
     * 分页查询
     *
     * @param syncHeight
     * @return
     */
    public QueryResult<SyncHeightPo> selectLimit(SyncHeightPo syncHeight);

    /**
     * 列表查询不分页
     *
     * @param syncHeight
     * @return
     */
    public List<SyncHeightPo> selectList(SyncHeightPo syncHeight);

    /**
     * 新增
     *
     * @param syncHeight
     * @return
     */
    public int insert(SyncHeightPo syncHeight);

    /**
     * 更新
     *
     * @param syncHeight
     * @return
     */
    public int update(SyncHeightPo syncHeight);

    /**
     * 更新
     *
     * @param set
     * @param where
     * @return
     */
    public int update(SyncHeightPo set, SyncHeightPo where);

    /**
     * 删除
     *
     * @param syncHeight
     * @return
     */
    public int delete(SyncHeightPo syncHeight);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public boolean batchInsert(List<SyncHeightPo> list);

    /**
     * 设置同步高度到指定高度
     *
     * @param height
     * @param targetBlockCache
     * @return
     */
    public boolean setSyncHeightTo(Integer height, BlockCachePo targetBlockCache);

    public void updateSyncHeight(MsgQueuePo msgQueuePo);

}
