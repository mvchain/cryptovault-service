package com.mvc.cryptovault.console.constant;

import java.math.BigInteger;

public interface BusinessConstant {

    /**
     * 上拉
     */
    Integer SEARCH_DIRECTION_UP = 0;
    /**
     * 下拉
     */
    Integer SEARCH_DIRECTION_DOWN = 1;
    /**
     * 项目等待购买
     */
    Integer APP_PROJECT_STATUS_WAIT = 0;
    /**
     * 项目订单前缀
     */
    String APP_PROJECT_ORDER_NUMBER = "APP_PROJECT_ORDER_NUMBER_";

    /**
     *  vrt
     */
    BigInteger BASE_TOKEN_ID_VRT = BigInteger.ONE;

    /**
     * 余额
     */
    BigInteger BASE_TOKEN_ID_BALANCE = BigInteger.valueOf(2);
    /**
     * ETH
     */
    BigInteger BASE_TOKEN_ID_ETH = BigInteger.valueOf(3);
    /**
     * USDT
     */
    BigInteger BASE_TOKEN_ID_USDT = BigInteger.valueOf(4);
    /**
     * 购买
     */
    Integer TRANSACTION_TYPE_BUY = 1;
    /**
     * 出售
     */
    Integer TRANSACTION_TYPE_SELL = 2;
    /**
     * 充值
     */
    Integer OPR_TYPE_RECHARGE= 1;
    /**
     * 提现
     */
    Integer OPR_TYPE_WITHDRAW = 2;
    /**
     * 取消状态
     */
    Integer STATUS_CANCEL = 4;
    /**
     * 区跨链类型
     */
    String CONTENT_BLOCK = "CONTENT_BLOCK";
    String CONTENT_PROJECT = "CONTENT_PROJECT";
    String CONTENT_TRANSACTION = "CONTENT_TRANSACTION";
    String CONTENT_EMIT = "CONTENT_EMIT";

    /**
     * 区跨链类型
     */
    Integer CLASSIFY_BLOCK = 0;
    String REDIS_QUEUE = "REDIS_QUEUE";
    String WITHDRAW_USDT_QUEUE = "WITHDRAW_USDT_QUEUE";
}
