package com.mvc.cryptovault.testdb.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.mvc.cryptovault.testdb.builder.BaseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author qiyichen
 * @create 2019/1/19 17:41
 */
public class DbUtil {

    static Logger logger = LoggerFactory.getLogger(DbUtil.class);
    private static DruidDataSource dataSource = null;
    private static Connection connection = null;
    private static Integer num = 10000;


    public static void init(String url, String userName, String password) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setMaxActive(10);
        druidDataSource.setInitialSize(1);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(3);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("select 'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(20);
        dataSource = druidDataSource;
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    public static void insert(Integer startNumber, Integer number, Class<? extends BaseBuilder> clazz) throws IllegalAccessException, InstantiationException, SQLException {
        insert(false, startNumber, number, clazz);
    }

    public static void insert(Boolean clear, Integer startNumber, Integer number, Class<? extends BaseBuilder> clazz) throws IllegalAccessException, InstantiationException, SQLException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Long start = System.currentTimeMillis();
                    Statement stat = connection.createStatement();
                    BaseBuilder obj = clazz.newInstance();
                    if (clear) {
                        String delSql = String.format("TRUNCATE TABLE %s;", obj.tableName());
                        stat.execute(delSql);
                    }
                    StringBuilder header = new StringBuilder(obj.getHeader());
                    for (int i = startNumber; i < number; i++) {
                        String sql = obj.getInstance(i + 1);
                        header = header.append(sql);
                        if ((i + 1) % num != 0 && i != number - 1) {
                            header.append(",");
                        }
                        if ((i + 1) % num == 0 || i == number - 1) {
                            stat.execute(header.toString());
                            connection.commit();
                            logger.info("{}数据{}条生成,耗时{}", clazz.getSimpleName(), num, (System.currentTimeMillis() - start) / 1000f);
                            header = new StringBuilder(obj.getHeader());
                            start = System.currentTimeMillis();
                        }
                    }
                    stat.close();
                    logger.info("{}数据数据生成完毕", clazz.getSimpleName());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        });
        thread.setName(clazz.getSimpleName());
        thread.start();
        logger.info("thread {} start", thread.getName());
    }

}
