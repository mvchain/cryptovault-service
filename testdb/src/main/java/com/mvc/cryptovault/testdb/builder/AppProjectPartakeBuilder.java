package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:02
 */
public class AppProjectPartakeBuilder extends BaseBuilder {
    public static final Integer NUMBER = AppUserBuilder.NUMBER;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s)", id, 1, id, 15, 100, 0.15, 0, 5);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_project_partake` (`id`, `project_id`, `user_id`, `value`, `times`, `reverse_value`, `publish_time`, `token_id`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_project_partake";
    }
}