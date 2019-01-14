package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * admin_user_permission
 */
@Table(name = "admin_user_permission")
@Data
public class AdminUserPermission implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private BigInteger permissionId;

    /**
     * admin_user_permission
     */
    private static final long serialVersionUID = 1L;

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
     * 权限id
     * @return permission_id 权限id
     */
    public BigInteger getPermissionId() {
        return permissionId;
    }

    /**
     * 权限id
     * @param permissionId 权限id
     */
    public void setPermissionId(BigInteger permissionId) {
        this.permissionId = permissionId;
    }
}