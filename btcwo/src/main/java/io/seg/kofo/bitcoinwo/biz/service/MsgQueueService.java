
package io.seg.kofo.bitcoinwo.biz.service;

import io.seg.kofo.bitcoinwo.dao.po.BlockCachePo;
import io.seg.kofo.bitcoinwo.dao.po.MsgQueuePo;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import io.seg.framework.dao.model.QueryResult;

import java.net.MalformedURLException;
import java.util.List;

public interface MsgQueueService {

     /**
      * 根据唯一索引查询
      * @param msgQueue
      * @return
      */
     public MsgQueuePo selectOne(MsgQueuePo msgQueue);

     /**
      * 分页查询
      * @param msgQueue
      * @return
      */
     public QueryResult<MsgQueuePo> selectLimit(MsgQueuePo msgQueue);

     /**
      * 列表查询不分页
      * @param msgQueue
      * @return
      */
     public List<MsgQueuePo> selectList(MsgQueuePo msgQueue);

     /**
      * 新增
      * @param msgQueue
      * @return
      */
     public int insert(MsgQueuePo msgQueue);

     /**
      * 更新
      * @param msgQueue
      * @return
      */
     public int update(MsgQueuePo msgQueue);

     /**
      * 更新
      * @param set
      * @param where
      * @return
      */
     public int update(MsgQueuePo set, MsgQueuePo where);

     /**
      * 删除
      * @param msgQueue
      * @return
      */
     public int delete(MsgQueuePo msgQueue);

     /**
      * 批量插入
      * @param list
      * @return
      */
     public boolean batchInsert(List<MsgQueuePo> list);

     /**
      * 统计消息队列长度
      * @return
      */
     public int countMsgQueue();

     /**
      * 获取生成区块同步信息
      * @return
      */
     public MsgQueuePo generateBlockMsg(Bitcoin.Block syncBlock);

     /**
      * 保存区块同步信息
      * @param msgQueuePo
      * @return
      */
     public boolean saveBlockInfo(MsgQueuePo msgQueuePo);
     /**
      * 生成分叉信息
      * 删除侧链blockCache
      * 调整last_call_back_height为分叉高度
      * @return
      */
     public MsgQueuePo generateForkingMsg(Bitcoin.Block forkingBlock);

     /**
      * 先进先出 获取1条msg进行回调处理
      * @return
      */
     public List<MsgQueuePo> getMsgForCallBack();

     /**
      * 更新回调过的msg以及记录回调高度
      * @param updatedMsg
      * @return
      */
     public boolean updateCallBackInfo(MsgQueuePo updatedMsg);

}
