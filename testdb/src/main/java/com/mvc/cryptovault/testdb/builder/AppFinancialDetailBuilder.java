package com.mvc.cryptovault.testdb.builder;

/**
 * @author qiyichen
 * @create 2019/1/19 16:01
 */
public class AppFinancialDetailBuilder extends BaseBuilder {
    public static final Integer NUMBER = 100;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, %s, %s)", id, 1, 5);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_financial_detail` (`financial_id`, `depth`, `ratio`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_financial_detail";
    }
}
