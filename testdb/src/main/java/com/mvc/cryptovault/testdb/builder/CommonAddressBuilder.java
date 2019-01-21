package com.mvc.cryptovault.testdb.builder;

import com.mvc.cryptovault.common.bean.CommonAddress;

/**
 * @author qiyichen
 * @create 2019/1/19 16:06
 */
public class CommonAddressBuilder extends BaseBuilder<CommonAddress> {
    public static final Integer NUMBER = 50000000;

    @Override
    public String getInstance(Integer id) {
        return String.format("(%s, '%s', '%s', %s, %s, %s, '%s', %s)", id, "ETH", "0x" + id, 1, 0, 0, "ETH", 1);
    }

    @Override
    public String getHeader() {
        return "INSERT INTO `common_address` (`id`, `token_type`, `address`, `used`, `balance`, `user_id`, `address_type`, `approve`) VALUES ";
    }

    @Override
    public String tableName() {
        return "common_address";
    }
}
