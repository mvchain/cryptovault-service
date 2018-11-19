package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppProjectUserTransaction;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface AppProjectUserTransactionMapper extends MyMapper<AppProjectUserTransaction> {
    @Select("select ifnull(sum(value),0) from app_project_user_transaction where user_id = #{userId} and project_id = #{projectId} and result in(0,1)")
    BigDecimal sum(@Param("userId") BigInteger userId, @Param("projectId")BigInteger id);
}