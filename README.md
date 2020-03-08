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
- Bitcoin Core v0.15+
- Omni Core v0.5.0+

## kofo内部依赖包

- 安装到本地运行：
    - mvn install:install-file -DgroupId=io.seg.framework -DartifactId=framework-dao-mybatis -Dversion=1.0.1-SNAPSHOT -Dfile=lib/framework-dao-mybatis-1.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true
    - mvn install:install-file -Dgrou1pId=io.seg.framework -DartifactId=framework-dao-api -Dversion=1.0.1-SNAPSHOT -Dfile=lib/framework-dao-api-1.0.1-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true
    - mvn install:install-file -DgroupId=io.seg -DartifactId=seg-bitcoin-json-rpc-client -Dversion=1.12.2-SNAPSHOT -Dfile=lib/seg-bitcoin-json-rpc-client-1.12.2-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true
    - mvn install:install-file -DgroupId=io.seg.kofo -DartifactId=kofo-common -Dversion=1.0-SNAPSHOT -Dfile=lib/kofo-common-1.0-SNAPSHOT.jar -Dpackaging=jar -DgeneratePom=true

## 表结构
- 见 btcwo/src/main/resources/script/btc-watchonly.sql
- 其中sync_height表保存当前同步高度及区块hash（单条记录），同步区块定时任务会更新该条记录。
- btc_block_height表记录当前节点高度状况、外部节点高度、当前业务回调高度等。只有一条记录。
- block_cache表记录同步到的高度和hash，分叉逻辑依赖该表。
- msg_queue表模拟回调业务方的消息队列，串行回调成功后有定时任务清理该表。

## 启动步骤
- 依赖apollo管理配置
- 配置项参考btcwo/src/main/resources/application.properties中注释部分，将其配置到apollo中启动即可

## 接口介绍
- /estimateFee 估算手续费
- /queryBlockMsg 根据区块高度查询区块详情
- /lastestHeight 查询当前全节点高度
- /queryId 根据txId查询交易详情
- /queryOmniBalance 查询omni资产的余额
- /replayAtHeight 指定高度重放区块并回调业务方，缓存区块内容
- /sendTransaction 广播交易

## 定时任务介绍
- BlockCompensationJob 区块补偿任务，指定起止高度后同步该区间内的区块。
- BlockHeightUpdateJob 检查更新全节点高度，记录外部api提供的全网高度。
- MsgCallBackDataFlowJob 将区块信息发送给业务方。
- MsgCleanSimpleJob 清理发送成功的区块信息。
- MsgGeneratorDataFlowJob 区块信息同步任务，保持一直在同步最新区块。