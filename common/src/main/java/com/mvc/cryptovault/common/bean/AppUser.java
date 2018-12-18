package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import lombok.Data;
import lombok.Generated;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * app_user
 */
@Table(name = "app_user")
@Data
public class AppUser implements Serializable {
    /**
     * 用户id
     */
    @Id

    @Column(name = "id")
    private BigInteger id;

    /**
     * 用户手机
     */
    @Column(name = "cellphone")
    private String cellphone;

    /**
     * 登录密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 用户头像地址
     */
    @Column(name = "head_image")
    private String headImage;

    /**
     * 交易密码
     */
    @Column(name = "transaction_password")
    private String transactionPassword;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

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
     * 账号状态
     */
    @Column(name = "status")
    private Integer status;

    private BigInteger vpUserId;
    /**
     * app_user
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     *
     * @return id 用户id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 用户id
     *
     * @param id 用户id
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 用户手机
     *
     * @return cellphone 用户手机
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * 用户手机
     *
     * @param cellphone 用户手机
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * 登录密码
     *
     * @return password 登录密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 登录密码
     *
     * @param password 登录密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 交易密码
     *
     * @return transaction_password 交易密码
     */
    public String getTransactionPassword() {
        return transactionPassword;
    }

    /**
     * 交易密码
     *
     * @param transactionPassword 交易密码
     */
    public void setTransactionPassword(String transactionPassword) {
        this.transactionPassword = transactionPassword;
    }

    /**
     * 昵称
     *
     * @return nickname 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}