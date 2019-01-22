package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:00
 */
public class AppFinancialBuilder extends BaseBuilder {

    public static final Integer NUMBER = 100;

    @Override
    public String getInstance(Integer id) {
        String sql2 = "(%s, '%s', %s, %s, '%s', '%s', %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)";
        String sql = String.format(sql2,
                id,
                "理财" + id,
                1,
                5,
                "JYWD",
                "VRT",
                0.3f,
                1,
                10,
                20,
                0,
                0,
                10000,
                100,
                50,
                0,
                0,
                0,
                2,
                0,
                1
        );
        return sql;
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_financial` " +
                "(" +
                "`id`, " +
                "`name`, " +
                "`base_token_id`, " +
                "`token_id`, " +
                "`base_token_name`, " +
                "`token_name`, " +
                "`ratio`, " +
                "`depth`, " +
                "`income_min`, " +
                "`income_max`, " +
                "`start_at`, " +
                "`stop_at`, " +
                "`limit_value`, " +
                "`user_limit`, " +
                "`times`, " +
                "`min_value`, " +
                "`created_at`, " +
                "`updated_at`, " +
                "`status`, " +
                "`sold`, " +
                "`visible`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_financial";
    }
}
