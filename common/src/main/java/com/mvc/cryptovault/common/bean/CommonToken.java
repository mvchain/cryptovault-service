package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * common_token
 */
@Table(name = "common_token")
@Data
public class CommonToken implements Serializable {
    /**
     * 
     */
    @Id

    @Column(name = "id")
    private BigInteger id;

    /**
     * 令牌名称
     */
    @Column(name = "token_name")
    private String tokenName;

    /**
     * 令牌中文
     */
    @Column(name = "token_cn_name")
    private String tokenCnName;

    /**
     * 令牌英文
     */
    @Column(name = "token_en_name")
    private String tokenEnName;

    /**
     * 令牌图片地址
     */
    @Column(name = "token_image")
    private String tokenImage;

    /**
     * 令牌类型，如ETH,BTC
     */
    @Column(name = "token_type")
    private String tokenType;

    /**
     * hash前缀
     */
    @Column(name = "link")
    private String link;

    /**
     * 令牌位数，代币使用
     */
    @Column(name = "token_decimal")
    private Integer tokenDecimal;

    /**
     * 合约类型（代币属性）
     */
    @Column(name = "token_contract_address")
    private String tokenContractAddress;

    /**
     * 排序id
     */
    @Column(name = "index_id")
    private Integer indexId;

    /**
     * 是否展示
     */
    @Column(name = "visible")
    private Integer visible;

    /**
     * 是否可冲提
     */
    @Column(name = "withdraw")
    private Integer withdraw;

    /**
     * 是否可冲提
     */
    @Column(name = "recharge")
    private Integer recharge;

    /**
     * 提现手续费（对用户，区块链手续费另行设置）
     */
    @Column(name = "fee")
    private Float fee;

    /**
     * 实际转账手续费（用于区块链转账)
     */
    @Column(name = "transafer_fee")
    private Float transaferFee;

    /**
     * 单笔提币下限
     */
    @Column(name = "withdraw_min")
    private BigDecimal withdrawMin;

    /**
     * 单笔提币上限
     */
    @Column(name = "withdraw_max")
    private BigDecimal withdrawMax;

    /**
     * 每日提币上限
     */
    @Column(name = "withdraw_day")
    private BigDecimal withdrawDay;

    /**
     * 删除标记位
     */
    @Column(name = "delete_status")
    private Integer deleteStatus;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    /**
     * 保留金额
     */
    private Float hold;
    /**
     * common_token
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
     * 令牌名称
     * @return token_name 令牌名称
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * 令牌名称
     * @param tokenName 令牌名称
     */
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * 令牌图片地址
     * @return token_image 令牌图片地址
     */
    public String getTokenImage() {
        return tokenImage;
    }

    /**
     * 令牌图片地址
     * @param tokenImage 令牌图片地址
     */
    public void setTokenImage(String tokenImage) {
        this.tokenImage = tokenImage;
    }

    /**
     * 令牌类型，如ETH,BTC
     * @return token_type 令牌类型，如ETH,BTC
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * 令牌类型，如ETH,BTC
     * @param tokenType 令牌类型，如ETH,BTC
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * 令牌位数，代币使用
     * @return token_decimal 令牌位数，代币使用
     */
    public Integer getTokenDecimal() {
        return tokenDecimal;
    }

    /**
     * 令牌位数，代币使用
     * @param tokenDecimal 令牌位数，代币使用
     */
    public void setTokenDecimal(Integer tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
    }

    /**
     * 合约类型（代币属性）
     * @return token_contract_address 合约类型（代币属性）
     */
    public String getTokenContractAddress() {
        return tokenContractAddress;
    }

    /**
     * 合约类型（代币属性）
     * @param tokenContractAddress 合约类型（代币属性）
     */
    public void setTokenContractAddress(String tokenContractAddress) {
        this.tokenContractAddress = tokenContractAddress;
    }

    /**
     * 排序id
     * @return index_id 排序id
     */
    public Integer getIndexId() {
        return indexId;
    }

    /**
     * 排序id
     * @param indexId 排序id
     */
    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
    }

    /**
     * 是否可冲提
     * @return withdraw 是否可冲提
     */
    public Integer getWithdraw() {
        return withdraw;
    }

    /**
     * 是否可冲提
     * @param withdraw 是否可冲提
     */
    public void setWithdraw(Integer withdraw) {
        this.withdraw = withdraw;
    }

    /**
     * 是否可冲提
     * @return recharge 是否可冲提
     */
    public Integer getRecharge() {
        return recharge;
    }

    /**
     * 是否可冲提
     * @param recharge 是否可冲提
     */
    public void setRecharge(Integer recharge) {
        this.recharge = recharge;
    }

    /**
     * 提现手续费（对用户，区块链手续费另行设置）
     * @return fee 提现手续费（对用户，区块链手续费另行设置）
     */
    public Float getFee() {
        return fee;
    }

    /**
     * 提现手续费（对用户，区块链手续费另行设置）
     * @param fee 提现手续费（对用户，区块链手续费另行设置）
     */
    public void setFee(Float fee) {
        this.fee = fee;
    }

    /**
     * 实际转账手续费（用于区块链转账)
     * @return transafer_fee 实际转账手续费（用于区块链转账)
     */
    public Float getTransaferFee() {
        return transaferFee;
    }

    /**
     * 实际转账手续费（用于区块链转账)
     * @param transaferFee 实际转账手续费（用于区块链转账)
     */
    public void setTransaferFee(Float transaferFee) {
        this.transaferFee = transaferFee;
    }

    /**
     * 单笔提币下限
     * @return withdraw_min 单笔提币下限
     */
    public BigDecimal getWithdrawMin() {
        return withdrawMin;
    }

    /**
     * 单笔提币下限
     * @param withdrawMin 单笔提币下限
     */
    public void setWithdrawMin(BigDecimal withdrawMin) {
        this.withdrawMin = withdrawMin;
    }

    /**
     * 单笔提币上限
     * @return withdraw_max 单笔提币上限
     */
    public BigDecimal getWithdrawMax() {
        return withdrawMax;
    }

    /**
     * 单笔提币上限
     * @param withdrawMax 单笔提币上限
     */
    public void setWithdrawMax(BigDecimal withdrawMax) {
        this.withdrawMax = withdrawMax;
    }

    /**
     * 每日提币上限
     * @return withdraw_day 每日提币上限
     */
    public BigDecimal getWithdrawDay() {
        return withdrawDay;
    }

    /**
     * 每日提币上限
     * @param withdrawDay 每日提币上限
     */
    public void setWithdrawDay(BigDecimal withdrawDay) {
        this.withdrawDay = withdrawDay;
    }
}