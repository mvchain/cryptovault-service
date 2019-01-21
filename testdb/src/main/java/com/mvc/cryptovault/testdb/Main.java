package com.mvc.cryptovault.testdb;

import com.mvc.cryptovault.testdb.builder.*;
import com.mvc.cryptovault.testdb.util.DbUtil;

import java.sql.SQLException;

/**
 * @author qiyichen
 * @create 2019/1/19 14:54
 */
public class Main {


    static {
        try {
            DbUtil.init("jdbc:mysql://192.168.15.31:3306/vpay_test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC", "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
        DbUtil.insert(true, 0, AppFinancialBuilder.NUMBER, AppFinancialBuilder.class);
        DbUtil.insert(true, 0, AppFinancialContentBuilder.NUMBER, AppFinancialContentBuilder.class);
        DbUtil.insert(true, 0, AppFinancialDetailBuilder.NUMBER, AppFinancialDetailBuilder.class);
        DbUtil.insert(true, 0, AppMessageBuilder.NUMBER, AppMessageBuilder.class);
        DbUtil.insert(true, 0, AppOrderBuilder.NUMBER, AppOrderBuilder.class);
        DbUtil.insert(true, 0, AppProjectPartakeBuilder.NUMBER, AppProjectPartakeBuilder.class);
        DbUtil.insert(true, 0, AppProjectUserTransactionBuilder.NUMBER, AppProjectUserTransactionBuilder.class);
        DbUtil.insert(true, 0, AppUserAddressBuilder.NUMBER, AppUserAddressBuilder.class);
        DbUtil.insert(true, 0, AppUserBalanceBuilder.NUMBER, AppUserBalanceBuilder.class);
        DbUtil.insert(true, 0, AppUserBuilder.NUMBER, AppUserBuilder.class);
        DbUtil.insert(true, 0, AppUserFinancialIncomeBuilder.NUMBER, AppUserFinancialIncomeBuilder.class);
        DbUtil.insert(true, 0, AppUserFinancialPartakeBuilder.NUMBER, AppUserFinancialPartakeBuilder.class);
        DbUtil.insert(true, 0, AppUserInviteBuilder.NUMBER, AppUserInviteBuilder.class);
        DbUtil.insert(true, 0, AppUserTransactionBuilder.NUMBER, AppUserTransactionBuilder.class);
        DbUtil.insert(true, 0, BlockTransactionBuilder.NUMBER, BlockTransactionBuilder.class);
        DbUtil.insert(true, 0, CommonAddressBuilder.NUMBER, CommonAddressBuilder.class);
    }

}
