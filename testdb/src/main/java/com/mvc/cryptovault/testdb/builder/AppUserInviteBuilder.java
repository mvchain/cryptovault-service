package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:04
 */
public class AppUserInviteBuilder extends BaseBuilder {

    public static final Integer NUMBER = AppUserBuilder.NUMBER;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s, %s)", id, id, 0, 0);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_user_invite` (`user_id`, `invite_user_id`, `created_at`, `updated_at`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_user_invite";
    }

}
