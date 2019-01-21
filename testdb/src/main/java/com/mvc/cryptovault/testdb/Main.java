package com.mvc.cryptovault.testdb;

import com.mvc.cryptovault.testdb.builder.AppFinancialBuilder;
import com.mvc.cryptovault.testdb.util.DbUtil;

import java.sql.SQLException;

/**
 * @author qiyichen
 * @create 2019/1/19 14:54
 */
public class Main {

    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
        DbUtil.init("jdbc:mysql://192.168.15.31:3306/vpay_test?useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC", "root", "123456");
        for (int i = 0; i < 10; i++) {
            Runnable runable = newRunnable(i == 0, i * 100000, (i + 1) * 100000, AppFinancialBuilder.class);
            new Thread(runable).start();
            System.out.println("进程已添加");
        }

//        DbUtil.insert(100000, 100000, AppFinancialBuilder.class);
//        TDHSClient client = new TDHSClientImpl(new InetSocketAddress("192.168.15.31", 9999), 2);


    }


    private static Runnable newRunnable(final Boolean clear, final Integer start, final Integer end, final Class clazz) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    DbUtil.insert(clear, start, end, clazz);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
