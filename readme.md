# btc watch-only 

## 主要功能
- 提供交易查询和交易广播接口
- 提供区块相关信息查询接口
- 同步区块,生成区块信息，生成分叉消息，缓存区块信息
- 可配置连接多个自建btc全节点，自动选取可用节点

## 外部依赖
- mysql v5.7
- apollo v2.0
- zookeeper
- eureka 
- sequence 全局序列号产生服务
- Bitcoin Core v0.15+
- Omni Core v0.5.0+

## 接口介绍
- /estimateFee 估算手续费
- /queryBlockMsg 根据区块高度查询区块详情
- /lastestHeight 查询当前全节点高度
- /queryId 根据txId查询交易详情
- /queryOmniBalance 查询omni资产的余额
- /replayAtHeight 指定高度重放区块，缓存区块内容
- /sendTransaction 广播交易

## 定时任务介绍
- BlockCompensationJob 区块补偿任务，指定起止高度后同步该区间内的区块。
- BlockHeightUpdateJob 检查更新全节点高度，记录外部api提供的全网高度。
- MsgCallBackDataFlowJob 将区块信息发送给业务方。
- MsgCleanSimpleJob 清理发送成功的区块信息。
- MsgGeneratorDataFlowJob 区块信息同步任务，保持一直在同步最新区块。
