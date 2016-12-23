1.dt_account 库


DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_no` varchar(50) NOT NULL,
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户编号',
  `balance` decimal(20,6) NOT NULL COMMENT '余额',
  `unbalance` decimal(20,6) NOT NULL,
  `security_money` decimal(20,6) NOT NULL,
  `total_income` decimal(20,6) NOT NULL COMMENT '总收益',
  `total_expend` decimal(20,6) NOT NULL COMMENT '总支出',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金账户表';

LOCK TABLES `account` WRITE;

INSERT INTO `account` (`id`, `account_no`, `merchant_id`, `balance`, `unbalance`, `security_money`, `total_income`, `total_expend`, `created_at`, `updated_at`)
VALUES
	(1,'',1,0.000000,0.000000,0.000000,4.000000,0.000000,'2016-12-13 17:06:45','2016-12-23 15:25:27');

UNLOCK TABLES;


DROP TABLE IF EXISTS `account_history`;

CREATE TABLE `account_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `account_no` varchar(50) NOT NULL DEFAULT '' COMMENT '账户编号',
  `merchant_id` int(11) DEFAULT NULL COMMENT '商家编号',
  `amount` decimal(20,6) NOT NULL COMMENT '流水金额',
  `balance` decimal(20,6) NOT NULL COMMENT '交易后余额',
  `request_no` varchar(36) NOT NULL DEFAULT '' COMMENT '银行银行订单号',
  `bank_trx_no` varchar(50) DEFAULT NULL COMMENT '银行交易流水号',
  `trx_type` varchar(36) NOT NULL DEFAULT '' COMMENT '业务类型',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` varchar(36) DEFAULT NULL COMMENT '0 预处理 1已确认 2取消',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金账户流水表';


2.dt_order库



DROP TABLE IF EXISTS `order_record`;

CREATE TABLE `order_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) DEFAULT NULL COMMENT '商家名称',
  `merchant_id` int(11) NOT NULL COMMENT '商户编号',
  `order_no` varchar(30) NOT NULL COMMENT '商户订单号',
  `product_name` varchar(300) DEFAULT NULL COMMENT '商品名称',
  `creater` varchar(100) DEFAULT NULL COMMENT '下单人',
  `status` int(11) DEFAULT NULL COMMENT '订单状态(0待支付1支付成功2支付失败3取消)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `order_amount` decimal(20,6) DEFAULT '0.000000' COMMENT '订单金额',
  `order_from` varchar(30) DEFAULT NULL COMMENT '订单来源',
  `order_ip` varchar(30) DEFAULT NULL COMMENT '下单IP',
  `plat_income` decimal(20,6) DEFAULT NULL COMMENT '平台收入',
  `fee_rate` decimal(20,6) DEFAULT NULL COMMENT '费率',
  `plat_cost` decimal(20,6) DEFAULT NULL COMMENT '平台成本',
  `plat_profit` decimal(20,6) DEFAULT NULL COMMENT '平台利润',
  `complete_time` timestamp NULL DEFAULT NULL COMMENT '支付完成时间',
  `trx_no` varchar(50) DEFAULT NULL COMMENT '支付流水号',
  `bank_order_no` varchar(50) DEFAULT NULL COMMENT '银行订单号',
  `bank_trx_no` varchar(50) DEFAULT NULL COMMENT '银行流水号',
  `payer_user_no` varchar(50) DEFAULT NULL COMMENT '付款人编号',
  `payer_name` varchar(60) DEFAULT NULL COMMENT '付款人名称',
  `payer_pay_amount` decimal(20,6) DEFAULT '0.000000' COMMENT '付款方支付金额',
  `payer_fee` decimal(20,6) DEFAULT '0.000000' COMMENT '付款方手续费',
  `payer_account_type` varchar(30) DEFAULT NULL COMMENT '付款方账户类型(参考账户类型枚举:AccountTypeEnum)',
  `receiver_user_no` varchar(15) DEFAULT NULL COMMENT '收款人编号',
  `receiver_name` varchar(60) DEFAULT NULL COMMENT '收款人名称',
  `receiver_pay_amount` decimal(20,6) DEFAULT '0.000000' COMMENT '收款方支付金额',
  `receiver_fee` decimal(20,6) DEFAULT '0.000000' COMMENT '收款方手续费',
  `bank_return_msg` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付记录表';

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) DEFAULT NULL COMMENT '商家名称',
  `merchant_id` int(11) NOT NULL COMMENT '商户编号',
  `order_no` varchar(30) NOT NULL COMMENT '商户订单号',
  `product_name` varchar(300) DEFAULT NULL COMMENT '商品名称',
  `creater` varchar(100) DEFAULT NULL COMMENT '下单人',
  `status` int(11) DEFAULT NULL COMMENT '订单状态(0待支付1正在支付2支付成功3支付失败4取消)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `order_amount` decimal(20,6) DEFAULT '0.000000' COMMENT '订单金额',
  `order_from` varchar(30) DEFAULT NULL COMMENT '订单来源',
  `order_ip` varchar(50) DEFAULT NULL COMMENT '下单IP',
  `referer_url` varchar(100) DEFAULT NULL COMMENT '从哪个页面链接过来的(可用于防诈骗)',
  `return_url` varchar(600) DEFAULT NULL COMMENT '页面回调通知URL',
  `notify_url` varchar(600) DEFAULT NULL COMMENT '后台异步通知URL',
  `cancel_reason` varchar(600) DEFAULT NULL COMMENT '订单撤销原因',
  `order_period` smallint(6) DEFAULT NULL COMMENT '订单有效期(单位分钟)',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '支付备注',
  `trx_type` varchar(30) DEFAULT NULL COMMENT '交易业务类型  ：消费、充值等',
  `pay_way` int(11) DEFAULT NULL COMMENT '支付方式',
  `fund_into_type` varchar(30) DEFAULT NULL COMMENT '资金流入类型',
  `is_refund` int(11) DEFAULT '0' COMMENT '是否退款(0无 1是)',
  `refund_times` int(11) DEFAULT '0' COMMENT '退款次数(默认值为:0)',
  `success_refund_amount` decimal(20,6) DEFAULT NULL COMMENT '成功退款总金额',
  `trx_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_KEY_2` (`order_no`,`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付订单表';


3.dt_accouning库

DROP TABLE IF EXISTS `accounting`;

CREATE TABLE `accounting` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `voucher_no` varchar(32) DEFAULT NULL COMMENT '支付流水号',
  `profit` decimal(24,10) DEFAULT NULL COMMENT '利润',
  `income` decimal(24,10) DEFAULT NULL COMMENT '收入',
  `cost` decimal(24,10) DEFAULT NULL COMMENT '成本',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `bank_change_amount` decimal(24,10) DEFAULT NULL COMMENT '平台银行帐户变动金额(订单金额)',
  `receiver_account_no` varchar(20) DEFAULT NULL COMMENT '收款商家id',
  `bank_order_no` varchar(32) DEFAULT NULL COMMENT '银行订单号',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `voucher_no` (`voucher_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会计原始凭证表';


4.dt_base库

DROP TABLE IF EXISTS `merchant_info`;

CREATE TABLE `merchant_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL,
  `account_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_Key_2` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户信息表';

LOCK TABLES `merchant_info` WRITE;

INSERT INTO `merchant_info` (`id`, `user_name`, `account_id`, `created_at`, `updated_at`)
VALUES
	(1,'商家1',1,'2016-12-12 00:54:53','2016-12-12 00:54:53');

UNLOCK TABLES;


DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) DEFAULT NULL COMMENT '商户编号',
  `status` int(11) DEFAULT '0' COMMENT '商品状态 0失效 1生效',
  `name` varchar(200) NOT NULL COMMENT '商品名称',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品表';

LOCK TABLES `product` WRITE;

INSERT INTO `product` (`id`, `merchant_id`, `status`, `name`, `created_at`, `updated_at`)
VALUES
	(1,1,1,'商品名称1','2016-12-11 23:58:14','2016-12-11 23:58:27');

UNLOCK TABLES;


5.dt_message库


DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` int(50) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `message_id` varchar(50) NOT NULL DEFAULT '' COMMENT '消息ID',
  `message_body` longtext NOT NULL COMMENT '消息内容',
  `message_data_type` varchar(50) NOT NULL DEFAULT '' COMMENT '消息数据类型',
  `consumer_queue` varchar(100) NOT NULL DEFAULT '' COMMENT '消费队列名称',
  `message_send_times` smallint(6) NOT NULL DEFAULT '0' COMMENT '消息重发次数',
  `is_dead` int(2) NOT NULL DEFAULT '0' COMMENT '0 未死亡 1已死亡',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态 0待确认 1已确认 2已消费',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `field1` varchar(200) DEFAULT '' COMMENT '查询字段1',
  `field2` varchar(200) DEFAULT '' COMMENT '查询字段2',
  `field3` varchar(200) DEFAULT '' COMMENT '查询字段3',
  `editor` varchar(100) DEFAULT NULL COMMENT '修改者',
  `creater` varchar(100) DEFAULT NULL COMMENT '创建来源',
  PRIMARY KEY (`id`),
  KEY `message_id` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


6.dt_tcc库


DROP TABLE IF EXISTS `tcc_transaction_account`;

CREATE TABLE `tcc_transaction_account` (
  `TRANSACTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DOMAIN` varchar(100) DEFAULT NULL,
  `GLOBAL_TX_ID` varbinary(32) NOT NULL,
  `BRANCH_QUALIFIER` varbinary(32) NOT NULL,
  `CONTENT` varbinary(8000) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `TRANSACTION_TYPE` int(11) DEFAULT NULL,
  `RETRIED_COUNT` int(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`TRANSACTION_ID`),
  UNIQUE KEY `UX_TX_BQ` (`GLOBAL_TX_ID`,`BRANCH_QUALIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `tcc_transaction_order`;

CREATE TABLE `tcc_transaction_order` (
  `TRANSACTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DOMAIN` varchar(100) DEFAULT NULL,
  `GLOBAL_TX_ID` varbinary(32) NOT NULL,
  `BRANCH_QUALIFIER` varbinary(32) NOT NULL,
  `CONTENT` varbinary(8000) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `TRANSACTION_TYPE` int(11) DEFAULT NULL,
  `RETRIED_COUNT` int(11) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `LAST_UPDATE_TIME` datetime DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`TRANSACTION_ID`),
  UNIQUE KEY `UX_TX_BQ` (`GLOBAL_TX_ID`,`BRANCH_QUALIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
