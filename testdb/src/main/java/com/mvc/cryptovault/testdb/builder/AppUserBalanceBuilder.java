package com.mvc.cryptovault.testdb.builder;

import com.mvc.cryptovault.common.bean.AppUserBalance;

/**
 * @author qiyichen
 * @create 2019/1/19 16:03
 */
public class AppUserBalanceBuilder  extends BaseBuilder<AppUserBalance>{
    public static final Integer NUMBER = AppUserBuilder.NUMBER;

    @Override
    public String getInstance(Integer id) {
        return String.format(" (%s, %s, %s, %s, %s)",  id, 5, 10000, 1, 0);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_user_balance` (`user_id`, `token_id`, `balance`, `visible`, `pending_balance`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_user_balance";
    }
}
