目的: 解决分布式事务的demo (可靠消息服务 tcc)

用到的技术:springboot spring mybais dubbo activeMq redis 开源项目tcc zookeeper

基础模块 accounting message user account     order模块依赖其它模块 同时本身也是个provider

流程 生成订单-->支付订单(发送银行请求,等待银行回调)-->银行回调接口(发送mq ODER_NOTIFY到common-mq-app模块)-->调用orderServiceRPC(完成支付订单,给商家+金额)

核心部分:调用orderServiceRPC
     使用可靠消息 生成会计流水(preSave预存储,confirm确认存储,定时器恢复机制)  使用tcc(try,confirm,cancel)来处理订单支付状态和商家余额的最终一致性.
     common-mq-app是mq处理端,也负责定时任务作可靠消息
     springboot-app是web接口端,里面的test代码MainClass是入口
     common-config是公共的配置部分,需要自己去配置(resources_comp,resources_home多环境配置)默认是resources_home  log4j目前默认推送到logback里,可自行配置

顶级的pom.xml定义了自己的私仓..需要自行修改或者删除, alib_youNeed目录存了你可能下不到的jar,可以去配置引入本地jar