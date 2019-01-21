package com.mvc.cryptovault.testdb.builder;

import com.mvc.cryptovault.common.bean.AppUserFinancialIncome;

/**
 * @author qiyichen
 * @create 2019/1/19 16:03
 */
public class AppUserFinancialIncomeBuilder extends BaseBuilder<AppUserFinancialIncome> {
    public static final Integer NUMBER = 50000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s, %s, %s, %s, '%s', %s, %s)",
                id, getUserId(), 1, 0, 0, 5, "JYWD", 20, getPartakeId());
    }

    @Override
    public   String getHeader() {
        return "INSERT INTO `app_user_financial_income` (`id`, `user_id`, `financial_id`, `created_at`, `updated_at`, `token_id`, `token_name`, `value`, `partake_id`) VALUES ";
    }

    @Override
    public   String tableName() {
        return "app_user_financial_income";
    }
}
