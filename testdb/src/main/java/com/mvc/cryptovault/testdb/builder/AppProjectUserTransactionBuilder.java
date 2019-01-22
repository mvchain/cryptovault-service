package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:02
 */
public class AppProjectUserTransactionBuilder extends BaseBuilder {
    public static final Integer NUMBER = 50000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, '%s')",
                id, getUserId(), 1, 0, 0, 2, 2, 5, 5000, 0, 0, 0, getOrderNumber());
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_project_user_transaction` (`id`, `user_id`, `project_id`, `created_at`, `updated_at`, `pair_id`, `result`, `value`, `payed`, `success_value`, `success_payed`, `index`, `project_order_number`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_project_user_transaction";
    }
}
