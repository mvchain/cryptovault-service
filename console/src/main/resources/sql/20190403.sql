ALTER TABLE `app_user`
ADD COLUMN `google_secret`  varchar(64) NULL AFTER `public_key`;

ALTER TABLE `app_user`
ADD COLUMN `google_check`  int NULL AFTER `google_secret`;

