package com.mvc.cryptovault.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Generated;

/**
 * app_user_transaction
 */
@Table(name = "app_user_transaction")
@Data
public class AppUserTransaction implements Serializable {
    /**
     * 
     */
    @Id

    @Column(name = "id")
    private BigInteger id;

    /**
     * 交易对id
     */
    @Column(name = "pair_id")
    private BigInteger pairId;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private Long createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private Long updatedAt;

    /**
     * 订单号（对外展示）
     */
    @Column(name = "order_number")
    private String orderNumber;

    /**
     * 父id，1级挂单为0
     */
    @Column(name = "parent_id")
    private BigInteger parentId;

    /**
     * 交易量
     */
    @Column(name = "value")
    private BigDecimal value;

    /**
     * 成交量
     */
    @Column(name = "success_value")
    private BigDecimal successValue;

    /**
     * 挂单用户id
     */
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 交易对象用户id
     */
    @Column(name = "target_user_id")
    private BigInteger targetUserId;

    /**
     * 交易价格
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 交易类型1购买 2出售
     */
    @Column(name = "transaction_type")
    private Integer transactionType;

    /**
     * 是否为挂单方id
     */
    @Column(name = "self_order")
    private Integer selfOrder;

    /**
     * 订单状态 0未完成 1全部完成 4取消
     */
    @Column(name = "status")
    private Integer status;

    /**
     * app_user_transaction
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
     * 订单号（对外展示）
     * @return order_number 订单号（对外展示）
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * 订单号（对外展示）
     * @param orderNumber 订单号（对外展示）
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 父id，1级挂单为0
     * @return parent_id 父id，1级挂单为0
     */
    public BigInteger getParentId() {
        return parentId;
    }

    /**
     * 父id，1级挂单为0
     * @param parentId 父id，1级挂单为0
     */
    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    /**
     * 交易量
     * @return value 交易量
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 交易量
     * @param value 交易量
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 成交量
     * @return success_value 成交量
     */
    public BigDecimal getSuccessValue() {
        return successValue;
    }

    /**
     * 成交量
     * @param successValue 成交量
     */
    public void setSuccessValue(BigDecimal successValue) {
        this.successValue = successValue;
    }

    /**
     * 挂单用户id
     * @return user_id 挂单用户id
     */
    public BigInteger getUserId() {
        return userId;
    }

    /**
     * 挂单用户id
     * @param userId 挂单用户id
     */
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    /**
     * 交易对象用户id
     * @return target_user_id 交易对象用户id
     */
    public BigInteger getTargetUserId() {
        return targetUserId;
    }

    /**
     * 交易对象用户id
     * @param targetUserId 交易对象用户id
     */
    public void setTargetUserId(BigInteger targetUserId) {
        this.targetUserId = targetUserId;
    }

    /**
     * 交易价格
     * @return price 交易价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 交易价格
     * @param price 交易价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 交易类型1转入 2转出
     * @return transaction_type 交易类型1转入 2转出
     */
    public Integer getTransactionType() {
        return transactionType;
    }

    /**
     * 交易类型0转入 1转出
     * @param transactionType 交易类型1转入 2转出
     */
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * 订单状态 0未完成 1全部完成 4取消
     * @return status 订单状态 0未完成 1全部完成 4取消
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 订单状态 0未完成 1全部完成 4取消
     * @param status 订单状态 0未完成 1全部完成 4取消
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}