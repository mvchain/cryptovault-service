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
 * app_order
 */
@Table(name = "app_order")
@Data
public class AppOrder implements Serializable {
    /**
     * 订单id，内部展示
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 订单编号，对用户展示
     */
    @Column(name = "order_number")
    private String orderNumber;

    /**
     * 订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账]
     */
    @Column(name = "classify")
    private Integer classify;

    /**
     * 交易记录id
     */
    @Column(name = "order_content_id")
    private BigInteger orderContentId;

    /**
     * 交易记录内容名称
     */
    @Column(name = "order_content_name")
    private String orderContentName;

    /**
     * 令牌id
     */
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 1转入 2转出
     */
    @Column(name = "order_type")
    private Integer orderType;

    /**
     * 
     */
    @Column(name = "created_at")
    private Long createdAt;

    /**
     * 
     */
    @Column(name = "updated_at")
    private Long updatedAt;

    /**
     * 转账状态[0待打包 1确认中 2打包成功 9打包失败]
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 交易金额变动
     */
    @Column(name = "value")
    private BigDecimal value;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 交易hash
     */
    @Column(name = "hash")
    private String hash;

    /**
     * 
     */
    @Column(name = "from_address")
    private String fromAddress;

    /**
     * app_order
     */
    private static final long serialVersionUID = 1L;

    /**
     * 订单id，内部展示
     * @return id 订单id，内部展示
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 订单id，内部展示
     * @param id 订单id，内部展示
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 订单编号，对用户展示
     * @return order_number 订单编号，对用户展示
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * 订单编号，对用户展示
     * @param orderNumber 订单编号，对用户展示
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * 订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账]
     * @return classify 订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账]
     */
    public Integer getClassify() {
        return classify;
    }

    /**
     * 订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账]
     * @param classify 订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账]
     */
    public void setClassify(Integer classify) {
        this.classify = classify;
    }

    /**
     * 交易记录id
     * @return order_content_id 交易记录id
     */
    public BigInteger getOrderContentId() {
        return orderContentId;
    }

    /**
     * 交易记录id
     * @param orderContentId 交易记录id
     */
    public void setOrderContentId(BigInteger orderContentId) {
        this.orderContentId = orderContentId;
    }

    /**
     * 交易记录内容名称
     * @return order_content_name 交易记录内容名称
     */
    public String getOrderContentName() {
        return orderContentName;
    }

    /**
     * 交易记录内容名称
     * @param orderContentName 交易记录内容名称
     */
    public void setOrderContentName(String orderContentName) {
        this.orderContentName = orderContentName;
    }

    /**
     * 0转入 1转出
     * @return order_type 0转入 1转出
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * 0转入 1转出
     * @param orderType 0转入 1转出
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * 转账状态[0待打包 1确认中 2打包成功 9打包失败]
     * @return status 转账状态[0待打包 1确认中 2打包成功 9打包失败]
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 转账状态[0待打包 1确认中 2打包成功 9打包失败]
     * @param status 转账状态[0待打包 1确认中 2打包成功 9打包失败]
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 交易金额变动
     * @return value 交易金额变动
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 交易金额变动
     * @param value 交易金额变动
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 交易hash
     * @return hash 交易hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * 交易hash
     * @param hash 交易hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * 
     * @return from_address 
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * 
     * @param fromAddress 
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}