ALTER TABLE `app_user_balance`
ADD COLUMN `pending_balance`  decimal(40,20) NULL AFTER `visible`;

CREATE TABLE `app_user_invite` (
`user_id`  bigint(20) NULL DEFAULT NULL ,
`invite_user_id`  bigint(20) NULL DEFAULT NULL ,
`created_at`  bigint(20) NULL DEFAULT NULL ,
`updated_at`  bigint(20) NULL DEFAULT NULL
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
ROW_FORMAT=COMPACT
;

ALTER TABLE `app_user`
CHANGE COLUMN `vp_user_id` `invite_level`  int(20) NULL DEFAULT NULL AFTER `status`;

CREATE TABLE `app_financial_detail` (
`financial_id`  bigint(20) NOT NULL ,
`depth`  int(11) NULL DEFAULT NULL ,
`ratio`  float NULL DEFAULT NULL ,
PRIMARY KEY (`financial_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
ROW_FORMAT=COMPACT
;

CREATE TABLE `app_financial` (
`id`  bigint(20) NOT NULL AUTO_INCREMENT ,
`name`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
`base_token_id`  bigint(20) NULL DEFAULT NULL ,
`token_id`  bigint(20) NULL DEFAULT NULL ,
`base_token_name`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
`token_name`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
`ratio`  float NULL DEFAULT NULL ,
`depth`  int(11) NULL DEFAULT NULL ,
`income_min`  float NULL DEFAULT NULL ,
`income_max`  float NULL DEFAULT NULL ,
`start_at`  bigint(20) NULL DEFAULT NULL ,
`stop_at`  bigint(20) NULL DEFAULT NULL ,
`limit`  decimal(40,20) NULL DEFAULT NULL ,
`user_limit`  decimal(40,20) NULL DEFAULT NULL ,
`times`  int(11) NULL DEFAULT NULL ,
`min_value`  decimal(40,20) NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;

CREATE TABLE `app_user_financial_partake` (
`user_id`  bigint(20) NOT NULL ,
`financial_id`  bigint(20) NULL DEFAULT NULL ,
`created_at`  bigint(20) NULL DEFAULT NULL ,
`updated_at`  bigint(20) NULL DEFAULT NULL ,
`value`  decimal(40,20) NULL DEFAULT NULL ,
PRIMARY KEY (`user_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
ROW_FORMAT=COMPACT
;

CREATE TABLE `app_user_financial_income` (
`user_id`  bigint(20) NULL DEFAULT NULL ,
`financial_id`  bigint(20) NULL DEFAULT NULL ,
`created_at`  bigint(20) NULL DEFAULT NULL ,
`updated_at`  bigint(20) NULL DEFAULT NULL ,
`token_id`  bigint(20) NULL DEFAULT NULL ,
`token_name`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ,
`value`  decimal(40,20) NULL DEFAULT NULL
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8mb4 COLLATE=utf8mb4_general_ci
ROW_FORMAT=COMPACT
;

ALTER TABLE `app_user`
ADD UNIQUE INDEX `index_app_user_email` (`email`) ;

