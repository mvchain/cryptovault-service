package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppUserFinancialIncome;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface AppUserFinancialIncomeMapper extends MyMapper<AppUserFinancialIncome> {


    @Select("select ifnull(sum(value), 0) from app_user_financial_income where user_id = #{userId} and financial_id = #{id}")
    BigDecimal getIncome(@Param("userId") BigInteger userId, @Param("id") BigInteger id);

    @Select("SELECT IFNULL(sum(`value`),0) value, token_id  FROM app_user_financial_income where user_id = #{userId} and created_at >= #{startAt} and created_at < #{stopAt} GROUP BY token_id")
    List<AppUserFinancialIncome> getLast(@Param("userId") BigInteger userId, @Param("startAt") Long startAt, @Param("stopAt") Long stopAt);

    @Select("SELECT IFNULL(sum(`value`),0) value FROM app_user_financial_income where user_id = #{userId} and partake_id = #{id} and  created_at >= #{startAt} and created_at < #{stopAt}")
    List<AppUserFinancialIncome> getLastDay(@Param("userId") BigInteger userId,@Param("id") BigInteger id, @Param("startAt") Long startAt, @Param("stopAt") Long stopAt);
}