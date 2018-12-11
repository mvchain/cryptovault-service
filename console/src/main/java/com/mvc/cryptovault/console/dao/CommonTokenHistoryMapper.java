package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppKline;
import com.mvc.cryptovault.common.bean.CommonTokenHistory;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface CommonTokenHistoryMapper extends MyMapper<CommonTokenHistory> {

    @Select("select price from common_token_history where token_id = #{tokenId} and created_at < #{createdAt} order by id asc limit 1")
    BigDecimal get24HBefore(@Param("tokenId") BigInteger tokenId,@Param("createdAt") Long currentTimeMillis);

    @Select("select price from common_token_history where token_id = #{tokenId} order by id asc limit 1")
    BigDecimal getFirst(@Param("tokenId") BigInteger tokenId);

    @Select("select * from common_token_history where token_id = #{tokenId} and created_at >= #{klineTime} limit 1")
    CommonTokenHistory findByTime(@Param("tokenId") BigInteger tokenId, @Param("klineTime") Long klineTime);

    @Select("select * from common_token_history where token_id = #{tokenId} order by id desc limit 1")
    CommonTokenHistory findByLast(@Param("tokenId") BigInteger tokenId);
}