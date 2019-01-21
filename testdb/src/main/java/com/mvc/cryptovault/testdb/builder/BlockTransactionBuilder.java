package com.mvc.cryptovault.testdb.builder;

import com.mvc.cryptovault.common.bean.BlockTransaction;

/**
 * @author qiyichen
 * @create 2019/1/19 16:05
 */
public class BlockTransactionBuilder  extends BaseBuilder<BlockTransaction>{
    public static final Integer NUMBER = 1000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, '%s', %s, %s, %s, %s, %s, '%s', %s, %s, %s, %s, '%s', '%s', %s, '%s', '%s', '%s')",
                id, "test", 0, 0, 0, 0, 5, "ETH", (int)(Math.random() * 2) + 1, getUserId(), 2, 6, "test", "test", 5, "test", "test", getOrderNumber());
    }

    @Override
    public   String getHeader() {
        return "INSERT INTO `block_transaction` (`id`, `hash`, `created_at`, `updated_at`, `fee`, `height`, `token_id`, `token_type`, `opr_type`, `user_id`, `status`, `transaction_status`, `error_msg`, `error_data`, `value`, `from_address`, `to_address`, `order_number`) VALUES ";
    }

    @Override
    public  String tableName() {
        return "block_transaction";
    }
}
