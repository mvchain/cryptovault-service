package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.vo.SignSumVO;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

public interface BlockTransactionMapper extends MyMapper<BlockTransaction> {

    @Update("update block_transaction set status = 2,updated_at = #{current}, transaction_status = 5 where id = #{obj.id} and updated_at = #{obj.updatedAt}")
    int updateSuccess(@Param("obj") BlockTransaction obj, @Param("current") Long currentTimeMillis);

    @Update("update block_transaction set hash = #{hash},status=1,transaction_status=4,error_msg='',error_data='' where order_number = #{orderId}")
    int updateHash(@Param("orderId") String orderId, @Param("hash") String hash);

    @Update("UPDATE app_user_balance t1, block_transaction t2 SET t1.balance = t1.balance + t2.`value` WHERE t1.token_id = t2.token_id AND t2.id IN (${ids}) AND t2.transaction_status = 3")
    Integer returnValue(@Param("ids") String ids);

    @Update("update block_transaction set transaction_status = #{transactionStatus}, updated_at = #{currentTimeMillis} where id = #{id} and updated_at = #{now}")
    Integer updateTransactionStatus(@Param("transactionStatus") Integer transactionStatus, @Param("currentTimeMillis") Long currentTimeMillis, @Param("now") Long now, @Param("id")BigInteger id);

    @Select("SELECT t2.token_name, t2.id token_id, sum(t1.`value`) total, t2.transafer_fee, count(1) num FROM block_transaction t1, common_token t2 WHERE t1.opr_type = 2 AND t1.token_id = t2.id AND transaction_status in (2,6) GROUP BY t1.token_id")
    List<SignSumVO> exportSignSum();

}