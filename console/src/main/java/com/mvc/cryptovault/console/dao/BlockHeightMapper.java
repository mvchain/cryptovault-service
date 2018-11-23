package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.BlockHeight;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface BlockHeightMapper extends MyMapper<BlockHeight> {

    @Update("update block_height set hold = #{value} where token_id = #{tokenId}")
    void updateHold(@Param("tokenId") BigInteger tokenId, @Param("value") BigDecimal value);

    @Update("update block_height set fee = #{value} where token_id = #{tokenId}")
    void updateFee(@Param("tokenId") BigInteger tokenId, @Param("value") BigDecimal value);
}