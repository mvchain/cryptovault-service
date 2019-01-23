package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:02
 */
public class AppMessageBuilder extends BaseBuilder {
    public static final Integer NUMBER = 20000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, '%s', %s, '%s', %s, %s, %s, %s, %s, %s, %s, %s)", id, "测试消息1", 1, "TEST", 1, 1, 1, 1, 10, 0, getUserId(), 0);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_message` (`id`, `message`, `content_id`, `content_type`, `status`, `message_type`, `is_read`, `send_flag`, `created_at`, `updated_at`, `user_id`, `push_time`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_message";
    }
}