package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.BlockUsdtWithdrawQueue;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BlockUsdtWithdrawQueueMapper extends MyMapper<BlockUsdtWithdrawQueue> {

    @Update("update block_usdt_withdraw_queue set status = 1 where status = 0 and from_address = #{fromAddress}")
    Integer start(@Param("fromAddress") String fromAddress);

    @Select("select * from block_usdt_withdraw_queue where status in (0,1) and started_at <= #{currentTimeMillis} ")
    List<BlockUsdtWithdrawQueue> findStart(@Param("currentTimeMillis") Long startAt);
}