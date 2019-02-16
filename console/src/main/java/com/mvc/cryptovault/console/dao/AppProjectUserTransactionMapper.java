package com.mvc.cryptovault.console.dao;

import com.mvc.cryptovault.common.bean.AppProjectUserTransaction;
import com.mvc.cryptovault.common.bean.vo.ProjectPublishDetailVO;
import com.mvc.cryptovault.common.bean.vo.ProjectPublishVO;
import com.mvc.cryptovault.console.common.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface AppProjectUserTransactionMapper extends MyMapper<AppProjectUserTransaction> {
    @Select("select ifnull(sum(value),0) from app_project_user_transaction where user_id = #{userId} and project_id = #{projectId} and result in(0,1)")
    BigDecimal sum(@Param("userId") BigInteger userId, @Param("projectId") BigInteger id);

    @Select("select * from app_project_user_transaction where project_id = #{id} limit 1")
    AppProjectUserTransaction existTrans(@Param("id") BigInteger id);

    @Update("update app_project_user_transaction set success_value = #{obj.successValue},`result` = #{obj.result}, success_payed = #{obj.successPayed}, updated_at = #{nowTime} where id = #{obj.id} and updated_at = #{obj.updatedAt}")
    void updateSuccess(@Param("obj") AppProjectUserTransaction transaction, @Param("nowTime") Long nowTime);

    @Select("SELECT sum(payed) payed, sum(success_payed) success_payed, sum(`value`) `value`, sum(success_value) success_value FROM app_project_user_transaction WHERE user_id = #{userId} AND project_id = #{projectId}")
    ProjectPublishDetailVO getPublishDetail(@Param("userId") BigInteger userId, @Param("projectId") BigInteger projectId);

    @Select("SELECT t1.id project_id, t1.* FROM app_project t1, app_project_partake t2 WHERE t1.id = t2.project_id AND t2.user_id = #{userId} and t1.id < #{projectId}")
    List<ProjectPublishVO> getPublish(@Param("userId") BigInteger userId, @Param("projectId") BigInteger projectId);
}