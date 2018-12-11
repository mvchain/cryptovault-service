package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommonTokenMapper extends MyMapper<CommonToken> {

    @Select("select * from common_token where id > 2")
    List<CommonToken> findKlineToken();

}