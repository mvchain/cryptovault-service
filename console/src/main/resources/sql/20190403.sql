ALTER TABLE `app_user`
ADD COLUMN `google_secret`  varchar(64) NULL AFTER `public_key`;

ALTER TABLE `app_user`
ADD COLUMN `google_check`  int NULL AFTER `google_secret`;

ALTER TABLE `app_user`
ADD COLUMN `salt`  varchar(64) NULL AFTER `google_check`;

ALTER TABLE `app_financial`
DROP COLUMN `add_sold`,
ADD COLUMN `add_sold`  decimal(40,20) NULL AFTER `visible`;

ALTER TABLE `app_financial`
ADD COLUMN `show_income_min`  float NULL AFTER `add_sold`,
ADD COLUMN `show_income_max`  float NULL AFTER `show_income_min`;
