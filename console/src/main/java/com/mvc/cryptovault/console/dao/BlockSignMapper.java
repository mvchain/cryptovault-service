package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.BlockSign;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlockSignMapper extends MyMapper<BlockSign> {

    @Select("select * from block_sign where token_type = #{tokenType} and started_at <= #{currentTimeMillis} and status = 0 limit 1")
    BlockSign findOneByToken(@Param("tokenType") String tokenType,@Param("currentTimeMillis") Long currentTimeMillis);
}