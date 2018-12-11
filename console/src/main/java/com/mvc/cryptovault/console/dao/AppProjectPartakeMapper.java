package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppProjectPartake;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;

public interface AppProjectPartakeMapper extends MyMapper<AppProjectPartake> {

    @Select("select group_concat(project_id) from app_project_partake where user_id = #{userId}")
    String getTag(@Param("userId") BigInteger userId);
}