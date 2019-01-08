package com.mvc.cryptovault.console.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * JPush
 *
 * @author qiyichen
 * @create 2018/11/15 12:15
 */
@Service
@Log4j
public class JPushService {

    @Autowired
    JPushClient jPushClient;

    public Boolean send(String msg, BigInteger orderId, String... userId) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("orderId", String.valueOf(orderId));
        return send(msg, extra, userId);
    }

    /**
     * TODO 有发送频率限制,超过限制需要添加到队列等待发送
     *
     * @param msg
     * @param extra
     * @param userId
     * @return
     */
    public Boolean send(String msg, Map<String, String> extra, String... userId) {
        PushPayload payload = buildPush(msg, extra, userId);
        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("Got result - " + result);
            return true;
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
        //失败则添加到队列并重发
        return false;
    }

    private PushPayload buildPush(String msg, Map<String, String> extra, String... userId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(userId))
                .setNotification(Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder().setAlert(msg).addExtras(extra).build()).build())
                .setMessage(Message.newBuilder().setMsgContent(msg).addExtras(extra).build())
                .build();
    }

    public Boolean sendTag(String msg, BigInteger projectId) {
        HashMap<String, String> extra = new HashMap<>();
        extra.put("orderId", String.valueOf(0));
        PushPayload payload = buildTagPush(msg, extra, projectId);
        try {
            PushResult result = jPushClient.sendPush(payload);
            log.info("Got result - " + result);
            return true;
        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
        //失败则添加到队列并重发
        return false;
    }

    private PushPayload buildTagPush(String msg, Map<String, String> extra, BigInteger projectId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(String.valueOf(projectId)))
                .setMessage(Message.newBuilder().setMsgContent(msg).addExtras(extra).build())
                .build();
    }
}
