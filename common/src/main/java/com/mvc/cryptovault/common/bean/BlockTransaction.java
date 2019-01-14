package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * block_transaction
 */
@Table(name = "block_transaction")
@Data
public class BlockTransaction implements Serializable {
    /**
     * 区块链交易记录
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 交易hash
     */
    @Column(name = "hash")
    private String hash;

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
     * 手续费
     */
    @Column(name = "fee")
    private BigDecimal fee;

    /**
     * 打包区块高度
     */
    @Column(name = "height")
    private BigInteger height;

    /**
     * 令牌id
     */
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 令牌id
     */
    @Column(name = "token_type")
    private String tokenType;

    /**
     * 操作类型 1充值 2提现
     */
    @Column(name = "opr_type")
    private Integer oprType;

    /**
     * 对应用户id
     */
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 订单状态0打包中 1确认中 2确认完毕 9失败
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 交易状态1. 待审核2. 待签名（审核通过后3. 拒绝4. 正在提币（导入签名文件后5. 提币成功（交易确认成功后6. 失败")
     */
    @Column(name = "transaction_status")
    private Integer transactionStatus;

    /**
     * 错误原因
     */
    @Column(name = "error_msg")
    private String errorMsg;

    /**
     * 错误描述
     */
    @Column(name = "error_data")
    private String errorData;

    /**
     * 交易数量
     */
    @Column(name = "value")
    private BigDecimal value;

    /**
     * 来源地址
     */
    @Column(name = "from_address")
    private String fromAddress;

    /**
     * 目标地址
     */
    @Column(name = "to_address")
    private String toAddress;

    /**
     * 对外订单id
     */
    @Column(name = "order_number")
    private String orderNumber;

    /**
     * block_transaction
     */
    private static final long serialVersionUID = 1L;

    /**
     * 区块链交易记录
     * @return id 区块链交易记录
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 区块链交易记录
     * @param id 区块链交易记录
     */
    public void setId(BigInteger id) {
        this.id = id;
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
     * 手续费
     * @return fee 手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 手续费
     * @param fee 手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 打包区块高度
     * @return height 打包区块高度
     */
    public BigInteger getHeight() {
        return height;
    }

    /**
     * 打包区块高度
     * @param height 打包区块高度
     */
    public void setHeight(BigInteger height) {
        this.height = height;
    }

    /**
     * 令牌id
     * @return token_id 令牌id
     */
    public BigInteger getTokenId() {
        return tokenId;
    }

    /**
     * 令牌id
     * @param tokenId 令牌id
     */
    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * 操作类型 1充值 2提现
     * @return opr_type 操作类型 1充值 2提现
     */
    public Integer getOprType() {
        return oprType;
    }

    /**
     * 操作类型 1充值 2提现
     * @param oprType 操作类型 1充值 2提现 9钱包
     */
    public void setOprType(Integer oprType) {
        this.oprType = oprType;
    }

    /**
     * 对应用户id
     * @return user_id 对应用户id
     */
    public BigInteger getUserId() {
        return userId;
    }

    /**
     * 对应用户id
     * @param userId 对应用户id
     */
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    /**
     * 订单状态0打包中 1确认中 2确认完毕 9失败
     * @return status 订单状态0打包中 1确认中 2确认完毕 9失败
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 订单状态0打包中 1确认中 2确认完毕 9失败
     * @param status 订单状态0打包中 1确认中 2确认完毕 9失败
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 错误原因
     * @return error_msg 错误原因
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 错误原因
     * @param errorMsg 错误原因
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * 错误描述
     * @return error_data 错误描述
     */
    public String getErrorData() {
        return errorData;
    }

    /**
     * 错误描述
     * @param errorData 错误描述
     */
    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    /**
     * 交易数量
     * @return value 交易数量
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 交易数量
     * @param value 交易数量
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 来源地址
     * @return from_address 来源地址
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * 来源地址
     * @param fromAddress 来源地址
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

}