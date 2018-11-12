package com.mvc.cryptovault.console.bean;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * app_user_opr_log
 */
@Table(name = "app_user_opr_log")
@Data
public class AppUserOprLog implements Serializable {
    /**
     * 记录id
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
     * 操作类型
     */
    @Column(name = "opr_type")
    private String oprType;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private BigInteger createdAt;

    /**
     * 记录内容，json格式
     */
    @Column(name = "content")
    private String content;

    /**
     * app_user_opr_log
     */
    private static final long serialVersionUID = 1L;

    /**
     * 记录id
     * @return id 记录id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 记录id
     * @param id 记录id
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
     * 操作类型
     * @return opr_type 操作类型
     */
    public String getOprType() {
        return oprType;
    }

    /**
     * 操作类型
     * @param oprType 操作类型
     */
    public void setOprType(String oprType) {
        this.oprType = oprType;
    }

    /**
     * 创建时间
     * @return created_at 创建时间
     */
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    /**
     * 创建时间
     * @param createdAt 创建时间
     */
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 记录内容，json格式
     * @return content 记录内容，json格式
     */
    public String getContent() {
        return content;
    }

    /**
     * 记录内容，json格式
     * @param content 记录内容，json格式
     */
    public void setContent(String content) {
        this.content = content;
    }
}