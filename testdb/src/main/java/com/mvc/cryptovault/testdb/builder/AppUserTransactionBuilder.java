package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:04
 */
public class AppUserTransactionBuilder extends BaseBuilder {
    public static final Integer NUMBER = 50000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s, %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                id, 2, 0, 0, getOrderNumber(), 0, 1000, 0, getUserId(), getUserId(), 1, (int) (Math.random() * 2) + 1, 1, 2);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_user_transaction` (`id`, `pair_id`, `created_at`, `updated_at`, `order_number`, `parent_id`, `value`, `success_value`, `user_id`, `target_user_id`, `price`, `transaction_type`, `self_order`, `status`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_user_transaction";
    }
}
