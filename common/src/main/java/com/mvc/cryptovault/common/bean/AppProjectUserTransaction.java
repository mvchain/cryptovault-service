package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * app_project_user_transaction
 */
@Table(name = "app_project_user_transaction")
@Data
public class AppProjectUserTransaction implements Serializable {
    /**
     * 
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private BigInteger projectId;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Long createdAt;

    /**
     * 修改时间
     */
    @Column(name = "updated_at")
    private Long updatedAt;

    /**
     * 交易对id
     */
    @Column(name = "pair_id")
    private BigInteger pairId;

    /**
     * 结果-0等待 1成功 4取消 9失败
     */
    @Column(name = "result")
    private Integer result;

    /**
     * 参与数量
     */
    @Column(name = "value")
    private BigDecimal value;

    /**
     * 参与金额
     */
    @Column(name = "payed")
    private BigDecimal payed;

    /**
     * 参与数量
     */
    @Column(name = "success_value")
    private BigDecimal successValue;

    /**
     * 参与金额
     */
    @Column(name = "success_payed")
    private BigDecimal successPayed;

    /**
     * 排序位置
     */
    @Column(name = "`index`")
    private Integer index;

    /**
     * 订单id
     */
    @Column(name = "project_order_number")
    private String projectOrderNumber;

    /**
     * app_project_user_transaction
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return id 
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 用户id
     * @return user_id 用户id
     */
    public BigInteger getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    /**
     * 项目id
     * @return project_id 项目id
     */
    public BigInteger getProjectId() {
        return projectId;
    }

    /**
     * 项目id
     * @param projectId 项目id
     */
    public void setProjectId(BigInteger projectId) {
        this.projectId = projectId;
    }

    /**
     * 交易对id
     * @return pair_id 交易对id
     */
    public BigInteger getPairId() {
        return pairId;
    }

    /**
     * 交易对id
     * @param pairId 交易对id
     */
    public void setPairId(BigInteger pairId) {
        this.pairId = pairId;
    }

    /**
     * 结果-等待 1成功 4取消 9失败
     * @return result 结果-0等待 1成功 4取消 9失败
     */
    public Integer getResult() {
        return result;
    }

    /**
     * 结果-等待 1成功 4取消 9失败
     * @param result 结果-等待 1成功 4取消 9失败
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * 参与金额
     * @return value 参与金额
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 参与金额
     * @param value 参与金额
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 订单id
     * @return project_order_number 订单id
     */
    public String getProjectOrderNumber() {
        return projectOrderNumber;
    }

    /**
     * 订单id
     * @param projectOrderNumber 订单id
     */
    public void setProjectOrderNumber(String projectOrderNumber) {
        this.projectOrderNumber = projectOrderNumber;
    }
}