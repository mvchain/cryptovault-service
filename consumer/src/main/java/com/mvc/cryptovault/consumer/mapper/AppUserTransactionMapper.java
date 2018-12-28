package com.mvc.cryptovault.consumer.mapper;

import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.consumer.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface AppUserTransactionMapper extends MyMapper<AppUserTransaction> {

    @Update("update app_user_transaction set status = if(success_value + #{value} >=value, 1, 0), success_value = success_value + #{value}, updated_at = #{currentTimeMillis} where id = #{id}")
    Integer updateValue(@Param("id") BigInteger id, @Param("value") BigDecimal value, @Param("currentTimeMillis") Long currentTimeMillis);
}