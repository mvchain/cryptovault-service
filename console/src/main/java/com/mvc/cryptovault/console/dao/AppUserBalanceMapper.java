package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppUserBalance;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface AppUserBalanceMapper extends MyMapper<AppUserBalance> {

    @Update("update app_user_balance set balance = balance + #{value} where user_id = #{userId} and token_id = #{tokenId} and balance +  #{value} >= 0")
    Integer updateBalance(@Param("userId") BigInteger userId, @Param("tokenId") BigInteger baseTokenId, @Param("value") BigDecimal value);

    @Update("update app_user_balance set visible = #{status} where user_id = #{userId} and token_id in (${ids})")
    Integer updateVisiable(@Param("userId") BigInteger userId, @Param("ids") String addStr, @Param("status") Integer status);

}