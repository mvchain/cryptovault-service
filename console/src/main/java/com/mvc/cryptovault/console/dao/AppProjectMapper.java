package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

public interface AppProjectMapper extends MyMapper<AppProject> {

    @Update("update app_project set `status` = 1 WHERE started_at <= #{currentTimeMillis} and `status` = 0")
    Integer updateProjectStartStatus(@Param("currentTimeMillis") Long currentTimeMillis);

    @Update("update app_project set `status` = 2 WHERE stop_at <= #{currentTimeMillis} and `status` = 1")
    Integer updateProjectStopStatus(@Param("currentTimeMillis") Long currentTimeMillis);

    @Select("SELECT GROUP_CONCAT(id) FROM app_project WHERE project_name = #{projectName}")
    String findIdsByName(@Param("projectName") String projectName);

    @Select("SELECT t1.* FROM app_project t1, app_project_user_transaction t2 WHERE t1.id = t2.project_id AND t2.user_id = #{userId} ${str} GROUP BY t2.project_id ORDER BY project_id DESC LIMIT #{pageSize}")
    List<AppProject> getMyProject(@Param("userId") BigInteger userId, @Param("str")String str,@Param("pageSize") Integer pageSize);
}