package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BlockTransactionMapper extends MyMapper<BlockTransaction> {

    @Update("update block_transaction set status = 2,updated_at = #{current}, transaction_status = 4 where id = #{obj.id} and updated_at = #{obj.updatedAt}")
    int updateSuccess(@Param("obj") BlockTransaction obj, @Param("current") Long currentTimeMillis);
}