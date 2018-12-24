package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AppProjectMapper extends MyMapper<AppProject> {

    @Update("update app_project set `status` = 1 WHERE started_at <= #{currentTimeMillis} and `status` = 0")
    Integer updateProjectStartStatus(@Param("currentTimeMillis") Long currentTimeMillis);

    @Update("update app_project set `status` = 2 WHERE stop_at <= #{currentTimeMillis} and `status` = 1")
    Integer updateProjectStopStatus(@Param("currentTimeMillis") Long currentTimeMillis);

    @Select("SELECT GROUP_CONCAT(id) FROM app_project WHERE project_name = #{projectName}")
    String findIdsByName(@Param("projectName") String projectName);

}