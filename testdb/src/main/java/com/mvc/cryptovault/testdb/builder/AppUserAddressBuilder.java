package com.mvc.cryptovault.testdb.builder;


/**
 * @author qiyichen
 * @create 2019/1/19 16:03
 */
public class AppUserAddressBuilder extends BaseBuilder {
    public static final Integer NUMBER = AppUserBuilder.NUMBER;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, '%s')", id, 5, "0x" + id);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_user_address` (`user_id`, `token_id`, `address`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_user_address";
    }
}
