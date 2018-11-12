package com.mvc.cryptovault.common.bean;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * admin_user
 */
@Table(name = "admin_user")
@Data
public class AdminUser implements Serializable {
    /**
     * 
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 1有效 0无效
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 用户昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 
     */
    @Column(name = "created_at")
    private BigInteger createdAt;

    /**
     * 
     */
    @Column(name = "updated_at")
    private BigInteger updatedAt;

    /**
     * admin_user
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
     * 用户名
     * @return username 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 用户名
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 用户密码
     * @return password 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 用户密码
     * @param password 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 1有效 0无效
     * @return status 1有效 0无效
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 1有效 0无效
     * @param status 1有效 0无效
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 用户昵称
     * @return nickname 用户昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 用户昵称
     * @param nickname 用户昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 
     * @return created_at 
     */
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt 
     */
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return updated_at 
     */
    public BigInteger getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt 
     */
    public void setUpdatedAt(BigInteger updatedAt) {
        this.updatedAt = updatedAt;
    }
}