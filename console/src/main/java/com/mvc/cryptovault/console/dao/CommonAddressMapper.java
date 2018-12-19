package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommonAddressMapper extends MyMapper<CommonAddress> {

    @Select("select * from common_address where used = 0 and token_type = #{tokenType} limit 1")
    CommonAddress findUnUsed(@Param("tokenType") String tokenType);

    @Select("SELECT t1.* from common_address t1, common_token t2 WHERE t1.token_type = t2.token_name and t1.balance > t2.hold and t1.balance > t2.transafer_fee and user_id > 0 and t1.token_type = 'ETH'")
    List<CommonAddress> findColldect();
}