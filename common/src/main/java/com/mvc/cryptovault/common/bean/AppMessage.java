package com.mvc.cryptovault.common.bean;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * app_message
 */
@Table(name = "app_message")
@Data
public class AppMessage implements Serializable {
    /**
     * 消息id
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 消息内容
     */
    @Column(name = "message")
    private String message;

    /**
     * 消息关联内容id
     */
    @Column(name = "content_id")
    private BigInteger contentId;

    /**
     * 内容类型，用于页面跳转
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * 0失败 1成功
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 消息类型0普通消息 1推送消息
     */
    @Column(name = "message_type")
    private Integer messageType;

    /**
     * 已读状态标记
     */
    @Column(name = "is_read")
    private Integer isRead;

    /**
     * 是否已推送标记位
     */
    @Column(name = "send_flag")
    private Integer sendFlag;

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
     * 推送目标用户id，位0则不区分用户，为系统推送
     */
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 预约推送时间
     */
    @Column(name = "push_time")
    private BigInteger pushTime;

    /**
     * app_message
     */
    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     *
     * @return id 消息id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 消息id
     *
     * @param id 消息id
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 消息内容
     *
     * @return message 消息内容
     */
    public String getMessage() {
        return message;
    }

    /**
     * 消息内容
     *
     * @param message 消息内容
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 消息关联内容id
     *
     * @return content_id 消息关联内容id
     */
    public BigInteger getContentId() {
        return contentId;
    }

    /**
     * 消息关联内容id
     *
     * @param contentId 消息关联内容id
     */
    public void setContentId(BigInteger contentId) {
        this.contentId = contentId;
    }

    /**
     * 内容类型，用于页面跳转
     *
     * @return content_type 内容类型，用于页面跳转
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 内容类型，用于页面跳转
     *
     * @param contentType 内容类型，用于页面跳转
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 是否已推送标记位
     *
     * @return send_flag 是否已推送标记位
     */
    public Integer getSendFlag() {
        return sendFlag;
    }

    /**
     * 是否已推送标记位
     *
     * @param sendFlag 是否已推送标记位
     */
    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }

    /**
     * 推送目标用户id，位0则不区分用户，为系统推送
     *
     * @return user_id 推送目标用户id，位0则不区分用户，为系统推送
     */
    public BigInteger getUserId() {
        return userId;
    }

    /**
     * 推送目标用户id，位0则不区分用户，为系统推送
     *
     * @param userId 推送目标用户id，位0则不区分用户，为系统推送
     */
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    /**
     * 预约推送时间
     *
     * @return push_time 预约推送时间
     */
    public BigInteger getPushTime() {
        return pushTime;
    }

    /**
     * 预约推送时间
     *
     * @param pushTime 预约推送时间
     */
    public void setPushTime(BigInteger pushTime) {
        this.pushTime = pushTime;
    }
}