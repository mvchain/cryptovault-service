package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface FinancialMapper extends MyMapper<AppFinancial> {

    @Update("update app_financial set sold = sold + #{value} where id = #{id} and sold + #{value} <= limit_value")
    Integer updateSold( @Param("id")BigInteger id, @Param("value") BigDecimal value);

    @Update("update app_financial set `status` = 1 WHERE start_at <= #{currentTimeMillis} and `status` = 0")
    Integer updateProjectStartStatus(@Param("currentTimeMillis") Long currentTimeMillis);

    @Update("update app_financial set `status` = 2 WHERE stop_at <= #{currentTimeMillis} and `status` = 1")
    Integer updateProjectStopStatus(@Param("currentTimeMillis") Long currentTimeMillis);

}