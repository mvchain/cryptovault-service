package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;

public interface AppUserMapper extends MyMapper<AppUser> {

    @Update("update app_user set invite_level = if(invite_level < 20,invite_level + 1,20), invite_num = invite_num + 1 where id = #{id}")
    Integer updateInvite(@Param("id") BigInteger id);

}