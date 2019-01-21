package com.mvc.cryptovault.testdb.builder;

import com.mvc.cryptovault.common.bean.AppFinancialContent;

/**
 * @author qiyichen
 * @create 2019/1/19 16:01
 */
public class AppFinancialContentBuilder extends BaseBuilder<AppFinancialContent> {
    public static final Integer NUMBER = 100;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, '%s', '%s')", id, "说明" + id, "规则" + id);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `app_financial_content` (`financial_id`, `content`, `rule`) VALUES ";
    }

    @Override
    public String tableName() {
        return "app_financial_content";
    }
}
