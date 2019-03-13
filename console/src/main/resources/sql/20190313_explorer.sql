ALTER TABLE `app_user`
ADD COLUMN `public_key`  varchar(128) NULL COMMENT '用户公钥' AFTER `invite_num`;

DROP TABLE IF EXISTS `explorer_block_info`;
CREATE TABLE `explorer_block_info` (
  `id` bigint(20) NOT NULL COMMENT '区块信息id',
  `difficult` int(11) DEFAULT NULL COMMENT '难度',
  `transactions` int(11) DEFAULT NULL COMMENT '交易笔数',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `explorer_block_setting`;
CREATE TABLE `explorer_block_setting` (
  `id` bigint(20) NOT NULL COMMENT '区块设置记录id',
  `start_block` bigint(20) DEFAULT NULL COMMENT '开始有效区块',
  `min_transaction` int(11) DEFAULT NULL COMMENT '最小交易笔数',
  `max_transaction` int(11) DEFAULT NULL COMMENT '最大交易笔数',
  `min_difficult` int(11) DEFAULT NULL COMMENT '最小难度',
  `max_difficult` int(11) DEFAULT NULL COMMENT '最大难度',
  `total_transaction` bigint(20) DEFAULT NULL COMMENT '总交易笔数',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `total` bigint(20) DEFAULT NULL COMMENT '发型总量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `explorer_block_setting` VALUES ('1', '100', '20', '50', '1', '10', '29199465', 'v1.0', '100000000');

DROP TABLE IF EXISTS `explorer_block_transaction`;
CREATE TABLE `explorer_block_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '区块交易记录id',
  `hash` varchar(128) DEFAULT NULL COMMENT '交易hash',
  `block_id` json DEFAULT NULL,
  `from_user_id` bigint(20) DEFAULT NULL COMMENT '转出地址',
  `to_user_id` bigint(20) DEFAULT NULL COMMENT '转入地址',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `value` decimal(40,20) DEFAULT NULL COMMENT '数量',
  `token_id` bigint(20) DEFAULT NULL COMMENT '令牌id',
  `token_name` varchar(64) DEFAULT NULL COMMENT '令牌名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=177796 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `explorer_block_user`;
CREATE TABLE `explorer_block_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '区块用户id',
  `public_key` varchar(128) DEFAULT NULL COMMENT '公钥',
  `created_at` bigint(20) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=197316 DEFAULT CHARSET=latin1;
