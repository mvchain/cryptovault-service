package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface BlockTransactionMapper extends MyMapper<BlockTransaction> {

    @Update("update block_transaction set status = 2,updated_at = #{current}, transaction_status = 5 where id = #{obj.id} and updated_at = #{obj.updatedAt}")
    int updateSuccess(@Param("obj") BlockTransaction obj, @Param("current") Long currentTimeMillis);

    @Update("update block_transaction set hash = #{hash},status=1,transaction_status=2,error_msg='',error_data='' where order_number = #{orderId}")
    int updateHash(@Param("orderId") String orderId, @Param("hash") String hash);

}