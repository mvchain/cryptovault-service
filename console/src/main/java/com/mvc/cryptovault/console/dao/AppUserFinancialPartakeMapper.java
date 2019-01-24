package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppUserFinancialPartake;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface AppUserFinancialPartakeMapper extends MyMapper<AppUserFinancialPartake> {

    @Select("select ifnull(sum(value), 0) from app_user_financial_partake where user_id = #{userId} and financial_id = #{id}")
    BigDecimal getPartake(@Param("userId") BigInteger userId, @Param("id") BigInteger id);

    @Select("SELECT ifnull(sum(`value`), 0) value, IFNULL(sum(income),0) income, token_id, base_token_id  FROM app_user_financial_partake where user_id = #{userId} and status < 3 GROUP BY token_id, base_token_id")
    List<AppUserFinancialPartake> getBalance(@Param("userId") BigInteger userId);

    @Select("SELECT IFNULL(sum(income),0) income, token_id  FROM app_user_financial_partake where user_id = #{userId} GROUP BY token_id")
    List<AppUserFinancialPartake> getIncome(@Param("userId") BigInteger userId);

    @Update("update app_user_financial_partake set shadow_value = shadow_value + #{value}, updated_at = #{currentTimeMillis} where id = #{id} and updated_at = #{updatedAt}")
    Integer updateShadow(@Param("id") BigInteger id, @Param("value") BigDecimal incomeParent, @Param("currentTimeMillis") Long currentTimeMillis, @Param("updatedAt") Long updatedAt);
}