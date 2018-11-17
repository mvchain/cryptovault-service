package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface CommonAddressMapper extends MyMapper<CommonAddress> {

    @Select("select * from common_address where used = 0 and token_type = #{tokenType} limit 1")
    CommonAddress findUnUsed(@Param("tokenType") String tokenType);

}