ALTER TABLE `app_user`
ADD COLUMN `google_secret`  varchar(64) NULL AFTER `public_key`;

ALTER TABLE `app_user`
ADD COLUMN `google_check`  int NULL AFTER `google_secret`;

ALTER TABLE `app_user`
ADD COLUMN `salt`  varchar(64) NULL AFTER `google_check`;

