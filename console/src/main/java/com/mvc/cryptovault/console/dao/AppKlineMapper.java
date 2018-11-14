package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppKline;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface AppKlineMapper extends MyMapper<AppKline> {

    @Select("select * from app_kline where pair_id = #{pairId} and kline_time >= #{klineTime} limit 1")
    AppKline findByTime(@Param("pairId") BigInteger pairId, @Param("klineTime") Long klineTime);

}