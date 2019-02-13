/*
Navicat MySQL Data Transfer

Source Server         : test-local
Source Server Version : 50505
Source Host           : 192.168.15.31:3306
Source Database       : vpay_mycat_bak

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2019-01-22 14:01:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin_permission`
-- ----------------------------
DROP TABLE IF EXISTS `admin_permission`;
CREATE TABLE `admin_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_key` varchar(8) DEFAULT NULL COMMENT '权限key',
  `permission_name` varchar(8) DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of admin_permission
-- ----------------------------
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('1', 'GLKZ', 'GLKZ');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('2', 'CTKZ', '冲提控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('3', '用户控制', '用户控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('4', '币种控制', '币种控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('5', '众筹控制', '众筹控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('6', '交易控制', '交易控制');
INSERT INTO `admin_permission` (`id`, `permission_key`, `permission_name`) VALUES ('7', '理财控制', '理财控制');

-- ----------------------------
-- Table structure for `admin_user`
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '1鏈夋晥 0鏃犳晥',
  `nickname` varchar(32) DEFAULT NULL COMMENT '鐢ㄦ埛鏄电О',
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `admin_type` tinyint(4) DEFAULT NULL COMMENT '0瓒呯骇 1鏅€?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` (`id`, `username`, `password`, `status`, `nickname`, `created_at`, `updated_at`, `admin_type`) VALUES ('1', 'admin', '86f3059b228c8acf99e69734b6bb32cc', '1', '超级管理员', null, null, '0');

-- ----------------------------
-- Table structure for `admin_user_permission`
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_permission`;
CREATE TABLE `admin_user_permission` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '鐢ㄦ埛id',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '鏉冮檺id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of admin_user_permission
-- ----------------------------
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '1');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '2');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '3');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '4');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '5');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '6');
INSERT INTO `admin_user_permission` (`user_id`, `permission_id`) VALUES ('1', '7');

-- ----------------------------
-- Table structure for `admin_wallet`
-- ----------------------------
DROP TABLE IF EXISTS `admin_wallet`;
CREATE TABLE `admin_wallet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_hot` tinyint(4) DEFAULT NULL COMMENT '鏄惁涓虹儹閽卞寘',
  `address` varchar(64) DEFAULT NULL COMMENT '閽卞寘鍦板潃',
  `balance` decimal(40,20) DEFAULT NULL COMMENT '浣欓',
  `block_type` tinyint(4) DEFAULT NULL COMMENT '1ETH 2BTC',
  `pv_key` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of admin_wallet
-- ----------------------------

-- ----------------------------
-- Table structure for `app_financial`
-- ----------------------------
DROP TABLE IF EXISTS `app_financial`;
CREATE TABLE `app_financial` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `base_token_id` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `base_token_name` varchar(32) DEFAULT NULL,
  `token_name` varchar(32) DEFAULT NULL,
  `ratio` float DEFAULT NULL,
  `depth` int(11) DEFAULT NULL,
  `income_min` float DEFAULT NULL,
  `income_max` float DEFAULT NULL,
  `start_at` bigint(20) DEFAULT NULL,
  `stop_at` bigint(20) DEFAULT NULL,
  `limit_value` decimal(40,20) DEFAULT NULL,
  `user_limit` decimal(40,20) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  `min_value` decimal(40,20) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `sold` decimal(40,20) DEFAULT NULL,
  `visible` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of app_financial
-- ----------------------------
-- ----------------------------
-- Table structure for `app_financial_content`
-- ----------------------------
DROP TABLE IF EXISTS `app_financial_content`;
CREATE TABLE `app_financial_content` (
  `financial_id` bigint(20) NOT NULL,
  `content` text,
  `rule` text,
  PRIMARY KEY (`financial_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of app_financial_content
-- ----------------------------
-- ----------------------------
-- Table structure for `app_financial_detail`
-- ----------------------------
CREATE TABLE `app_financial_detail` (
`financial_id`  bigint(20) NOT NULL ,
`depth`  int(11) NOT NULL ,
`ratio`  float NULL DEFAULT NULL ,
PRIMARY KEY (`financial_id`, `depth`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
ROW_FORMAT=COMPACT
;

-- ----------------------------
-- Records of app_financial_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `app_message`
-- ----------------------------
DROP TABLE IF EXISTS `app_message`;
CREATE TABLE `app_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message` varchar(128) DEFAULT NULL COMMENT '娑堟伅鍐呭',
  `content_id` bigint(20) DEFAULT NULL COMMENT '娑堟伅鍏宠仈鍐呭id',
  `content_type` varchar(16) DEFAULT NULL COMMENT '鍐呭绫诲瀷锛岀敤浜庨〉闈㈣烦杞?',
  `status` tinyint(4) DEFAULT NULL COMMENT '0澶辫触 1鎴愬姛',
  `message_type` tinyint(4) DEFAULT NULL COMMENT '娑堟伅绫诲瀷0鏅€氭秷鎭?1鎺ㄩ€佹秷鎭?',
  `is_read` tinyint(4) DEFAULT NULL COMMENT '宸茶鐘舵€佹爣璁?',
  `send_flag` tinyint(4) DEFAULT NULL COMMENT '鏄惁宸叉帹閫佹爣璁颁綅',
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '鎺ㄩ€佺洰鏍囩敤鎴穒d锛屼綅0鍒欎笉鍖哄垎鐢ㄦ埛锛屼负绯荤粺鎺ㄩ€?',
  `push_time` bigint(20) DEFAULT NULL COMMENT '棰勭害鎺ㄩ€佹椂闂?',
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
ALTER TABLE app_message PARTITION by HASH(user_id) PARTITIONS 256; 

-- ----------------------------
-- Records of app_message
-- ----------------------------

-- ----------------------------
-- Table structure for `app_order`
-- ----------------------------
DROP TABLE IF EXISTS `app_order`;
CREATE TABLE `app_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '璁㈠崟id锛屽唴閮ㄥ睍绀?',
  `order_number` varchar(32) DEFAULT NULL COMMENT '璁㈠崟缂栧彿锛屽鐢ㄦ埛灞曠ず',
  `classify` tinyint(4) DEFAULT NULL COMMENT '璁㈠崟绉嶇被[0鍖哄潡閾句氦鏄?1璁㈠崟浜ゆ槗 2浼楃浜ゆ槗锛堝寘鍚紬绛瑰拰鐢变紬绛瑰紩璧风殑閲婃斁锛?鍒掕处]',
  `order_content_id` bigint(20) DEFAULT NULL COMMENT '浜ゆ槗璁板綍id',
  `order_content_name` varchar(32) DEFAULT NULL COMMENT '浜ゆ槗璁板綍鍐呭鍚嶇О',
  `token_id` bigint(20) DEFAULT NULL COMMENT '浠ょ墝id',
  `order_type` tinyint(4) DEFAULT NULL COMMENT ' 1杞叆 2杞嚭',
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '杞处鐘舵€乕0寰呮墦鍖?1纭涓?2鎵撳寘鎴愬姛 9鎵撳寘澶辫触]',
  `value` decimal(40,20) DEFAULT NULL COMMENT '浜ゆ槗閲戦鍙樺姩',
  `user_id` bigint(20) NOT NULL,
  `hash` varchar(128) DEFAULT NULL,
  `from_address` varchar(64) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `order_remark` varchar(32) DEFAULT NULL,
  `fee` decimal(40,20) DEFAULT NULL,
  `to_address` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
ALTER TABLE app_order PARTITION by HASH(user_id) PARTITIONS 256; 

-- ----------------------------
-- Records of app_order
-- ----------------------------

-- ----------------------------
-- Table structure for `app_project`
-- ----------------------------
DROP TABLE IF EXISTS `app_project`;
CREATE TABLE `app_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(32) DEFAULT NULL COMMENT '椤圭洰鍚嶇О',
  `base_token_id` bigint(20) DEFAULT NULL COMMENT '鍩虹甯佺id',
  `base_token_name` varchar(32) DEFAULT NULL COMMENT '鍩虹甯佺鍚嶇О',
  `token_id` bigint(20) DEFAULT NULL COMMENT '甯佺id',
  `token_name` varchar(32) DEFAULT NULL COMMENT '甯佺鍚嶇О',
  `project_image` varchar(128) DEFAULT NULL COMMENT '椤圭洰鍥剧墖',
  `pair_id` bigint(20) DEFAULT NULL COMMENT '浜ゆ槗瀵笽D',
  `created_at` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '椤圭洰鐘舵€?鍗冲皢寮€濮?1杩涜涓?2宸茬粨鏉?3鍙戝竵涓?9鍙栨秷',
  `updated_at` bigint(20) DEFAULT NULL,
  `visiable` tinyint(4) DEFAULT NULL COMMENT '鏄惁灞曠ず',
  `started_at` bigint(20) DEFAULT NULL COMMENT '寮€濮嬫椂闂?',
  `stop_at` bigint(20) DEFAULT NULL COMMENT '缁撴潫鏃堕棿',
  `publish_at` bigint(20) DEFAULT NULL COMMENT '鍙戝竵鏃堕棿',
  `project_total` decimal(40,20) DEFAULT NULL COMMENT '浼楃鏁伴噺',
  `ratio` float(40,20) DEFAULT NULL COMMENT '鍏戞崲姣斾緥',
  `release_value` float DEFAULT NULL COMMENT '閲婃斁姣斾緥',
  `project_limit` decimal(40,20) DEFAULT NULL COMMENT '闄愯喘鏁伴噺',
  `project_min` decimal(40,20) DEFAULT NULL COMMENT ' 鏈€灏忚喘涔版暟閲?',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of app_project
-- ----------------------------

-- ----------------------------
-- Table structure for `app_project_partake`
-- ----------------------------
DROP TABLE IF EXISTS `app_project_partake`;
CREATE TABLE `app_project_partake` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `value` decimal(40,20) DEFAULT NULL,
  `times` int(11) DEFAULT NULL COMMENT '寰呴噴鏀炬鏁?',
  `reverse_value` decimal(40,20) DEFAULT NULL COMMENT '鍗曟閲婃斁姣斾緥',
  `publish_time` bigint(20) DEFAULT NULL COMMENT '涓嬩竴娆″彂甯佹椂闂?',
  `token_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  UNIQUE KEY `index_partake` (`project_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE app_project_partake PARTITION by HASH(user_id) PARTITIONS 8; 

-- ----------------------------
-- Records of app_project_partake
-- ----------------------------

-- ----------------------------
-- Table structure for `app_project_user_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `app_project_user_transaction`;
CREATE TABLE `app_project_user_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '鐢ㄦ埛id',
  `project_id` bigint(20) DEFAULT NULL COMMENT '椤圭洰id',
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `pair_id` bigint(20) DEFAULT NULL COMMENT '浜ゆ槗瀵筰d',
  `result` tinyint(4) DEFAULT NULL COMMENT '缁撴灉-0绛夊緟 1鎴愬姛 4鍙栨秷 9澶辫触',
  `value` decimal(40,20) DEFAULT NULL COMMENT ' 鍙備笌鏁伴噺',
  `payed` decimal(40,20) DEFAULT NULL COMMENT '鍙備笌閲戦',
  `success_value` decimal(40,20) DEFAULT NULL COMMENT '鍙備笌鏁伴噺',
  `success_payed` decimal(40,20) DEFAULT NULL COMMENT ' 鍙備笌閲戦',
  `index` int(11) DEFAULT NULL COMMENT '鎺掑簭浣嶇疆',
  `project_order_number` varchar(64) DEFAULT NULL COMMENT '璁㈠崟id',
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE app_project_user_transaction PARTITION by HASH(user_id) PARTITIONS 8; 

-- ----------------------------
-- Records of app_project_user_transaction
-- ----------------------------

-- ----------------------------
-- Table structure for `app_user`
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cellphone` varchar(32) DEFAULT NULL COMMENT '鐢ㄦ埛鎵嬫満',
  `password` varchar(128) DEFAULT NULL,
  `head_image` varchar(128) DEFAULT NULL COMMENT '鐢ㄦ埛澶村儚鍦板潃',
  `transaction_password` varchar(128) DEFAULT NULL,
  `nickname` varchar(32) DEFAULT NULL COMMENT '鏄电О',
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '璐﹀彿鐘舵€?',
  `invite_level` int(20) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `pv_key` varchar(128) DEFAULT NULL,
  `invite_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE app_user PARTITION by HASH(id) PARTITIONS 2; 

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user` (`id`, `cellphone`, `password`, `head_image`, `transaction_password`, `nickname`, `created_at`, `updated_at`, `status`, `invite_level`, `email`, `pv_key`, `invite_num`) VALUES ('1', '18888888888', '123456', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '123456', '1', '1547639892945', '1547639892945', '1', '0', '416350145@qq.com', '770d38a17c11a3a06f99ae35abf712da', '0');

-- ----------------------------
-- Table structure for `app_user_address`
-- ----------------------------
DROP TABLE IF EXISTS `app_user_address`;
CREATE TABLE `app_user_address` (
  `user_id` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL COMMENT '浠ょ墝id',
  `address` varchar(64) DEFAULT NULL COMMENT '鏀舵鍦板潃'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE app_user_address PARTITION by HASH(user_id) PARTITIONS 8; 

-- ----------------------------
-- Records of app_user_address
-- ----------------------------

-- ----------------------------
-- Table structure for `app_user_balance`
-- ----------------------------
DROP TABLE IF EXISTS `app_user_balance`;
CREATE TABLE `app_user_balance` (
  `user_id` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL COMMENT '浠ょ墝id',
  `balance` decimal(40,20) DEFAULT NULL COMMENT '鏀舵鍦板潃',
  `visible` tinyint(4) DEFAULT NULL COMMENT '鏄惁鍦ㄨ祫浜у垪琛ㄤ腑灞曠ず',
  `pending_balance` decimal(40,20) DEFAULT NULL,
  PRIMARY KEY (`user_id`, `token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE app_user_balance PARTITION by HASH(user_id) PARTITIONS 8; 

-- ----------------------------
-- Table structure for `app_user_financial_income`
-- ----------------------------
DROP TABLE IF EXISTS `app_user_financial_income`;
CREATE TABLE `app_user_financial_income` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `financial_id` bigint(20) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `token_name` varchar(32) DEFAULT NULL,
  `value` decimal(40,20) DEFAULT NULL,
  `partake_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

ALTER TABLE app_user_financial_income PARTITION by HASH(user_id) PARTITIONS 256; 

-- ----------------------------
-- Records of app_user_financial_income
-- ----------------------------

-- ----------------------------
-- Table structure for `app_user_financial_partake`
-- ----------------------------
DROP TABLE IF EXISTS `app_user_financial_partake`;
CREATE TABLE `app_user_financial_partake` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `financial_id` bigint(20) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `value` decimal(40,20) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  `shadow_value` decimal(40,20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `income` decimal(40,20) DEFAULT NULL,
  `order_number` varchar(64) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `base_token_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

ALTER TABLE app_user_financial_partake PARTITION by HASH(user_id) PARTITIONS 256; 

-- ----------------------------
-- Records of app_user_financial_partake
-- ----------------------------

-- ----------------------------
-- Table structure for `app_user_invite`
-- ----------------------------
DROP TABLE IF EXISTS `app_user_invite`;
CREATE TABLE `app_user_invite` (
  `user_id` bigint(20) DEFAULT NULL,
  `invite_user_id` bigint(20) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE app_user_invite PARTITION by HASH(user_id) PARTITIONS 256; 

-- ----------------------------
-- Records of app_user_invite
-- ----------------------------

-- ----------------------------
-- Table structure for `app_user_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `app_user_transaction`;
CREATE TABLE `app_user_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pair_id` bigint(20) DEFAULT NULL COMMENT '浜ゆ槗瀵筰d',
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `order_number` varchar(64) DEFAULT NULL COMMENT '璁㈠崟鍙凤紙瀵瑰灞曠ず锛?',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '鐖秈d锛?绾ф寕鍗曚负0',
  `value` decimal(40,20) DEFAULT NULL COMMENT '浜ゆ槗閲?',
  `success_value` decimal(40,20) DEFAULT NULL COMMENT '鎴愪氦閲?',
  `user_id` bigint(20) NOT NULL COMMENT '鎸傚崟鐢ㄦ埛id',
  `target_user_id` bigint(20) DEFAULT NULL COMMENT '浜ゆ槗瀵硅薄鐢ㄦ埛id',
  `price` decimal(40,20) DEFAULT NULL COMMENT '浜ゆ槗浠锋牸',
  `transaction_type` tinyint(4) DEFAULT NULL COMMENT '浜ゆ槗绫诲瀷1璐拱 2鍑哄敭',
  `self_order` tinyint(4) DEFAULT NULL COMMENT '鏄惁涓烘寕鍗曟柟id',
  `status` tinyint(4) DEFAULT NULL COMMENT '璁㈠崟鐘舵€?0鏈畬鎴?1鍏ㄩ儴瀹屾垚 4鍙栨秷',
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE app_user_transaction PARTITION by HASH(user_id) PARTITIONS 256; 

-- ----------------------------
-- Records of app_user_transaction
-- ----------------------------

-- ----------------------------
-- Table structure for `block_hot_address`
-- ----------------------------
DROP TABLE IF EXISTS `block_hot_address`;
CREATE TABLE `block_hot_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(128) DEFAULT NULL,
  `pv_key` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of block_hot_address
-- ----------------------------

-- ----------------------------
-- Table structure for `block_sign`
-- ----------------------------
DROP TABLE IF EXISTS `block_sign`;
CREATE TABLE `block_sign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `opr_type` tinyint(4) DEFAULT NULL,
  `order_id` varchar(64) DEFAULT NULL,
  `sign` longtext,
  `result` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `hash` varchar(128) DEFAULT NULL,
  `started_at` bigint(20) DEFAULT NULL,
  `token_type` varchar(16) DEFAULT NULL,
  `contract_address` varchar(128) DEFAULT NULL,
  `from_address` varchar(64) DEFAULT NULL,
  `to_address` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
ALTER TABLE block_sign PARTITION by HASH(id) PARTITIONS 32; 

-- ----------------------------
-- Records of block_sign
-- ----------------------------

-- ----------------------------
-- Table structure for `block_transaction`
-- ----------------------------
DROP TABLE IF EXISTS `block_transaction`;
CREATE TABLE `block_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash` varchar(128) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `fee` decimal(40,20) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `token_type` varchar(32) DEFAULT NULL,
  `opr_type` tinyint(4) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `transaction_status` tinyint(4) DEFAULT NULL,
  `error_msg` varchar(128) DEFAULT NULL,
  `error_data` varchar(128) DEFAULT NULL,
  `value` decimal(60,20) DEFAULT NULL,
  `from_address` varchar(64) DEFAULT NULL,
  `to_address` varchar(64) DEFAULT NULL,
  `order_number` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE block_transaction PARTITION by HASH(id) PARTITIONS 32; 

-- ----------------------------
-- Records of block_transaction
-- ----------------------------

-- ----------------------------
-- Table structure for `block_usdt_withdraw_queue`
-- ----------------------------
DROP TABLE IF EXISTS `block_usdt_withdraw_queue`;
CREATE TABLE `block_usdt_withdraw_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) DEFAULT NULL,
  `from_address` varchar(128) DEFAULT NULL,
  `fee` decimal(40,20) DEFAULT NULL,
  `to_address` varchar(128) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0绛夊緟 1杩涘叆闃熷垪 2鎴愬姛 9澶辫触',
  `value` decimal(40,20) DEFAULT NULL,
  `started_at` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE block_usdt_withdraw_queue PARTITION by HASH(id) PARTITIONS 32; 

-- ----------------------------
-- Records of block_usdt_withdraw_queue
-- ----------------------------

-- ----------------------------
-- Table structure for `common_address`
-- ----------------------------
DROP TABLE IF EXISTS `common_address`;
CREATE TABLE `common_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token_type` varchar(16) DEFAULT NULL,
  `address` varchar(64) DEFAULT NULL,
  `used` tinyint(4) DEFAULT NULL,
  `balance` decimal(40,20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `address_type` varchar(16) DEFAULT NULL,
  `approve` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE common_address PARTITION by HASH(id) PARTITIONS 64; 

-- ----------------------------
-- Records of common_address
-- ----------------------------

-- ----------------------------
-- Table structure for `common_pair`
-- ----------------------------
DROP TABLE IF EXISTS `common_pair`;
CREATE TABLE `common_pair` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pair_name` varchar(64) DEFAULT NULL,
  `base_token_id` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  `base_token_name` varchar(32) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `token_name` varchar(32) DEFAULT NULL,
  `fee` decimal(40,20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of common_pair
-- ----------------------------
INSERT INTO `common_pair` (`id`, `pair_name`, `base_token_id`, `token_id`, `base_token_name`, `status`, `token_name`, `fee`) VALUES ('1', 'USDT/BZTB', '1', '4', 'BZTB', '1', 'USDT', '0.00000000000000000000');
INSERT INTO `common_pair` (`id`, `pair_name`, `base_token_id`, `token_id`, `base_token_name`, `status`, `token_name`, `fee`) VALUES ('5', 'ETH/BZTB', '1', '3', 'BZTB', '0', 'ETH', '0.00000000000000000000');

-- ----------------------------
-- Table structure for `common_token`
-- ----------------------------
DROP TABLE IF EXISTS `common_token`;
CREATE TABLE `common_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token_name` varchar(32) DEFAULT NULL,
  `token_cn_name` varchar(32) DEFAULT NULL,
  `token_en_name` varchar(32) DEFAULT NULL,
  `token_image` varchar(128) DEFAULT NULL,
  `token_type` varchar(16) DEFAULT NULL,
  `link` varchar(128) DEFAULT NULL,
  `token_decimal` int(11) DEFAULT NULL,
  `token_contract_address` varchar(64) DEFAULT NULL,
  `index_id` int(11) DEFAULT NULL,
  `visible` tinyint(4) DEFAULT NULL,
  `withdraw` tinyint(4) DEFAULT NULL,
  `recharge` tinyint(4) DEFAULT NULL,
  `fee` float DEFAULT NULL,
  `transafer_fee` float DEFAULT NULL,
  `withdraw_min` decimal(40,20) DEFAULT NULL,
  `withdraw_max` decimal(40,20) DEFAULT NULL,
  `withdraw_day` decimal(40,20) DEFAULT NULL,
  `delete_status` tinyint(4) DEFAULT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `updated_at` bigint(20) DEFAULT NULL,
  `hold` decimal(40,20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of common_token
-- ----------------------------
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('1', 'BZTB', 'BZTB', 'BZTB', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', null, '', '0', '1', '1', '1', '0', '0', '0.00000000000000000000', '1000.00000000000000000000', '5000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('2', 'BTC', '比特币', 'bitcoin', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', 'BTC', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', null, null, null, '1', '1', '1', '0', '0.00005', '0.00000000000000000000', '1000.00000000000000000000', '5000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('3', 'ETH', '以太坊', 'ethernum', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', 'ETH', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', null, null, '2', '1', '0', '0', '0', '1', '0.00000000000000000000', '0.00000000000000000000', '0.00000000000000000000', '0', '0', '0', '1.00000000000000000000');
INSERT INTO `common_token` (`id`, `token_name`, `token_cn_name`, `token_en_name`, `token_image`, `token_type`, `link`, `token_decimal`, `token_contract_address`, `index_id`, `visible`, `withdraw`, `recharge`, `fee`, `transafer_fee`, `withdraw_min`, `withdraw_max`, `withdraw_day`, `delete_status`, `created_at`, `updated_at`, `hold`) VALUES ('4', 'USDT', '泰达币', 'USDT', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', 'BTC', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=538598390,4205429837&fm=27&gp=0.jpg', '18', null, '3', '1', '1', '1', '0', '0', '0.00000000000000000000', '1000.00000000000000000000', '5000.00000000000000000000', '0', '0', '0', '0.00000000000000000000');

-- ----------------------------
-- Table structure for `common_token_control`
-- ----------------------------
DROP TABLE IF EXISTS `common_token_control`;
CREATE TABLE `common_token_control` (
  `token_id` bigint(20) NOT NULL,
  `increase_min` float DEFAULT NULL,
  `increase_max` float DEFAULT NULL,
  `decrease_min` float DEFAULT NULL,
  `decrease_max` float DEFAULT NULL,
  `buy_min` float DEFAULT NULL,
  `buy_max` float DEFAULT NULL,
  `sell_min` float DEFAULT NULL,
  `sell_max` float DEFAULT NULL,
  `price_base` decimal(40,20) DEFAULT NULL,
  `next_price` decimal(40,20) DEFAULT NULL,
  `wave_min` float DEFAULT NULL,
  `wave_max` float DEFAULT NULL,
  `start_price` decimal(40,20) DEFAULT NULL,
  `start_status` tinyint(4) DEFAULT NULL,
  `transaction_status` tinyint(4) DEFAULT NULL,
  `min_limit` decimal(40,20) DEFAULT NULL COMMENT '鏈€灏忚喘涔伴噺',
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of common_token_control
-- ----------------------------
INSERT INTO `common_token_control` (`token_id`, `increase_min`, `increase_max`, `decrease_min`, `decrease_max`, `buy_min`, `buy_max`, `sell_min`, `sell_max`, `price_base`, `next_price`, `wave_min`, `wave_max`, `start_price`, `start_status`, `transaction_status`, `min_limit`) VALUES ('1', '6', '10', '3', '5', '-10', '10', '-10', '10', '1.00000000000000000000', '0.00000000000000000000', '0.1', '0.2', '1.00000000000000000000', '1', '1', '0.00000000000000000000');
INSERT INTO `common_token_control` (`token_id`, `increase_min`, `increase_max`, `decrease_min`, `decrease_max`, `buy_min`, `buy_max`, `sell_min`, `sell_max`, `price_base`, `next_price`, `wave_min`, `wave_max`, `start_price`, `start_status`, `transaction_status`, `min_limit`) VALUES ('3', '6', '10', '3', '5', '-10', '10', '-10', '10', '1.00000000000000000000', '0.00000000000000000000', '0.1', '0.2', '1.00000000000000000000', '1', '1', '0.00000000000000000000');
INSERT INTO `common_token_control` (`token_id`, `increase_min`, `increase_max`, `decrease_min`, `decrease_max`, `buy_min`, `buy_max`, `sell_min`, `sell_max`, `price_base`, `next_price`, `wave_min`, `wave_max`, `start_price`, `start_status`, `transaction_status`, `min_limit`) VALUES ('4', '6', '10', '3', '5', '-10', '10', '-10', '10', '1.00000000000000000000', '0.00000000000000000000', '0.1', '0.2', '1.00000000000000000000', '1', '0', '0.00000000000000000000');
INSERT INTO `common_token_control` (`token_id`, `increase_min`, `increase_max`, `decrease_min`, `decrease_max`, `buy_min`, `buy_max`, `sell_min`, `sell_max`, `price_base`, `next_price`, `wave_min`, `wave_max`, `start_price`, `start_status`, `transaction_status`, `min_limit`) VALUES ('5', '6', '10', '3', '5', '-10', '10', '-10', '10', '100.00000000000000000000', '0.00000000000000000000', '0.1', '0.2', '1.00000000000000000000', '1', '1', '0.00000000000000000000');

-- ----------------------------
-- Table structure for `common_token_control_next`
-- ----------------------------
DROP TABLE IF EXISTS `common_token_control_next`;
CREATE TABLE `common_token_control_next` (
  `token_id` bigint(20) NOT NULL,
  `next_price` decimal(40,20) DEFAULT NULL,
  `next_type` tinyint(4) DEFAULT NULL,
  `total_success` decimal(60,20) DEFAULT NULL,
  `float_price` decimal(40,20) DEFAULT NULL COMMENT '涓嬩竴涓皬娴姩浠锋牸',
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of common_token_control_next
-- ----------------------------

-- ----------------------------
-- Table structure for `common_token_history`
-- ----------------------------
DROP TABLE IF EXISTS `common_token_history`;
CREATE TABLE `common_token_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token_id` bigint(20) NOT NULL,
  `created_at` bigint(20) DEFAULT NULL,
  `price` decimal(40,20) DEFAULT NULL,
  PRIMARY KEY (`id`,`token_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE common_token_history PARTITION by HASH(token_id) PARTITIONS 16; 

-- ----------------------------
-- Records of common_token_history
-- ----------------------------

-- ----------------------------
-- Table structure for `common_token_price`
-- ----------------------------
DROP TABLE IF EXISTS `common_token_price`;
CREATE TABLE `common_token_price` (
  `token_id` bigint(20) NOT NULL,
  `token_name` varchar(32) DEFAULT NULL,
  `token_price` decimal(40,20) DEFAULT NULL,
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of common_token_price
-- ----------------------------
INSERT INTO `common_token_price` (`token_id`, `token_name`, `token_price`) VALUES ('1', 'BZTB', '1.00000000000000000000');


-- ----------------------------
-- Table structure for `token_volume`
-- ----------------------------
DROP TABLE IF EXISTS `token_volume`;
CREATE TABLE `token_volume` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` bigint(20) DEFAULT NULL,
  `value` decimal(60,20) DEFAULT NULL,
  `token_id` bigint(20) NOT NULL,
  `used` tinyint(4) DEFAULT NULL COMMENT '鏄惁浣跨敤杩囪鏁版嵁',
  PRIMARY KEY (`id`,`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

ALTER TABLE token_volume PARTITION by HASH(token_id) PARTITIONS 256; 

-- ----------------------------
-- Records of token_volume
-- ----------------------------
        CREATE INDEX `index_message_1` ON `app_message`(`created_at`) USING BTREE ;
        CREATE INDEX `index_message_2` ON `app_message`(`content_id`, `content_type`) USING BTREE ;
        CREATE INDEX `index_app_order_1` ON `app_order`(`created_at`) USING BTREE ;
        CREATE INDEX `index_app_order_2` ON `app_order`(`order_number`, `classify`) USING BTREE ;
        CREATE INDEX `index_app_order_3` ON `app_order`(`token_id`) USING BTREE ;
        CREATE INDEX `index_app_order_4` ON `app_order`(`hash`) USING BTREE ;
        CREATE INDEX `index_app_order_5` ON `app_order`(`order_type`, `status`) USING BTREE ;
        CREATE INDEX `index_partake_2` ON `app_project_partake`(`token_id`) USING BTREE ;
        CREATE INDEX `index_partake_3` ON `app_project_partake`(`times`) USING BTREE ;
        CREATE INDEX `index_app_project_user_transaction_1` ON `app_project_user_transaction`(`project_id`) USING BTREE ;
        CREATE INDEX `index_app_project_user_transaction_2` ON `app_project_user_transaction`(`created_at`) USING BTREE ;
        CREATE INDEX `index_app_project_user_transaction_3` ON `app_project_user_transaction`(`project_order_number`) USING BTREE ;
        CREATE INDEX `index_app_project_user_transaction_4` ON `app_project_user_transaction`(`pair_id`, `result`, `index`) USING BTREE ;
        CREATE INDEX `index_app_user_1` ON `app_user`(`email`) USING BTREE ;
        CREATE INDEX `index_app_user_2` ON `app_user`(`nickname`) USING BTREE ;
        CREATE INDEX `index_app_user_3` ON `app_user`(`created_at`) USING BTREE ;
        CREATE INDEX `index_app_user_4` ON `app_user`(`pv_key`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_income_1` ON `app_user_financial_income`(`created_at`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_income_2` ON `app_user_financial_income`(`token_id`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_income_3` ON `app_user_financial_income`(`partake_id`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_partake_1` ON `app_user_financial_partake`(`token_id`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_partake_2` ON `app_user_financial_partake`(`created_at`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_partake_3` ON `app_user_financial_partake`(`financial_id`, `times`) USING BTREE ;
        CREATE INDEX `index_app_user_financial_partake_4` ON `app_user_financial_partake`(`order_number`) USING BTREE ;
        CREATE INDEX `index_app_user_invite_1` ON `app_user_invite`(`user_id`) USING BTREE ;
        CREATE INDEX `index_app_user_invite_2` ON `app_user_invite`(`invite_user_id`) USING BTREE ;
        CREATE INDEX `index_app_user_transaction_1` ON `app_user_transaction`(`created_at`) USING BTREE ;
        CREATE INDEX `index_app_user_transaction_2` ON `app_user_transaction`(`order_number`) USING BTREE ;
        CREATE INDEX `index_app_user_transaction_3` ON `app_user_transaction`(`parent_id`) USING BTREE ;
        CREATE INDEX `index_app_user_transaction_4` ON `app_user_transaction`(`target_user_id`) USING BTREE ;
        CREATE INDEX `index_app_user_transaction_5` ON `app_user_transaction`(`transaction_type`) USING BTREE ;
        CREATE INDEX `index_block_sign_1` ON `block_sign`(`started_at`) USING BTREE ;
        CREATE INDEX `index_block_sign_3` ON `block_sign`(`contract_address`) USING BTREE ;
        CREATE INDEX `index_block_sign_4` ON `block_sign`(`token_type`) USING BTREE ;
        CREATE INDEX `index_block_sign_5` ON `block_sign`(`hash`) USING BTREE ;
        CREATE INDEX `index_block_transaction_1` ON `block_transaction`(`created_at`) USING BTREE ;
        CREATE INDEX `index_block_transaction_2` ON `block_transaction`(`hash`) USING BTREE ;
        CREATE INDEX `index_block_transaction_3` ON `block_transaction`(`token_id`, `token_type`, `user_id`) USING BTREE ;
        CREATE INDEX `index_block_transaction_4` ON `block_transaction`(`from_address`) USING BTREE ;
        CREATE INDEX `index_block_usdt_withdraw_queue_1` ON `block_usdt_withdraw_queue`(`started_at`) USING BTREE ;
        CREATE INDEX `index_block_usdt_withdraw_queue_2` ON `block_usdt_withdraw_queue`(`status`) USING BTREE ;
        CREATE INDEX `index_block_usdt_withdraw_queue_3` ON `block_usdt_withdraw_queue`(`from_address`, `to_address`) USING BTREE ;
        CREATE INDEX `index_common_address_1` ON `common_address`(`token_type`) USING BTREE ;
        CREATE INDEX `index_common_address_2` ON `common_address`(`user_id`) USING BTREE ;
        CREATE INDEX `index_common_address_3` ON `common_address`(`address_type`) USING BTREE ;
        CREATE INDEX `index_common_token_history_1` ON `common_token_history`(`created_at`) USING BTREE ;
        CREATE INDEX `index_token_volume_1` ON `token_volume`(`created_at`) USING BTREE ;


CREATE TABLE `app_info` (
`app_type`  varchar(64) NULL ,
`app_version`  varchar(255) NULL ,
`app_version_code`  int NULL ,
`app_package`  varchar(64) NULL ,
PRIMARY KEY (`app_type`)
);
ALTER TABLE `app_info`
ADD COLUMN `http_url`  varchar(255) NULL AFTER `app_package`;


