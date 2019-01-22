package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:04
 */
public class AppUserFinancialPartakeBuilder extends BaseBuilder {
    public static final Integer NUMBER = 1000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s', %s, %s)",
                id, getUserId(), 1, 0, 0, 10, 13, 0, 2, 5, getOrderNumber(), 5, 1);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_user_financial_partake` (`id`, `user_id`, `financial_id`, `created_at`, `updated_at`, `value`, `times`, `shadow_value`, `status`, `income`, `order_number`, `token_id`, `base_token_id`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_user_financial_partake";
    }
}
