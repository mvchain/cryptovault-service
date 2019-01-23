package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:02
 */
public class AppOrderBuilder extends BaseBuilder {
    public static final Integer NUMBER = 20000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, '%s', %s, %s, '%s', %s, %s, %s, %s, %s, %s, %s, '%s', '%s', %s, '%s')", id, getOrderNumber(), (int) (Math.random() * 5), 3, "TEST", 5, (int) (Math.random() * 2) + 1, 10, 0, 2, 10, getUserId(), "", "", 0, "TEST" + id);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_order` (`id`, `order_number`, `classify`, `order_content_id`, `order_content_name`, `token_id`, `order_type`, `created_at`, `updated_at`, `status`, `value`, `user_id`, `hash`, `from_address`, `project_id`, `order_remark`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_order";
    }
}
