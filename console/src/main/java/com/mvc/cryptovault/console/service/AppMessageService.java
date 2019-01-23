package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppMessage;
import com.mvc.cryptovault.common.bean.AppOrder;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.TimeSearchDTO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.dao.AppMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppMessageService {

    private static final String MODEL_TRANSFER = "%s %s %s%s";
    private static final String MODEL_PROJECT = "%s%s %s %s %s";
    private static final String MODEL_PUBLISH = "%s %s %s金额已释放";
    private static final String MODEL_TRADE = "%s 成功%s %s %s";

    @Autowired
    JPushService jPushService;
    @Autowired
    AppMessageMapper appMessageMapper;

    public List<AppMessage> list(BigInteger userId, TimeSearchDTO timeSearchDTO, PageDTO pageDTO) {
        Condition condition = new Condition(AppMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        PageHelper.startPage(1, pageDTO.getPageSize());
        PageHelper.orderBy("created_at desc");
        if (timeSearchDTO.getType() == 0 && null != timeSearchDTO.getTimestamp()) {
            ConditionUtil.andCondition(criteria, "created_at > ", timeSearchDTO.getTimestamp());
        } else if (timeSearchDTO.getType() == 1 && null != timeSearchDTO.getTimestamp()) {
            ConditionUtil.andCondition(criteria, "created_at < ", timeSearchDTO.getTimestamp());
        }
        return appMessageMapper.selectByCondition(condition);
    }

    @Async
    public void transferFinancialMsg(String name, BigInteger id, BigInteger userId, BigDecimal value, String tokenName) {
        String msg = String.format(MODEL_TRANSFER, value.stripTrailingZeros().toPlainString(), tokenName, name + " 理财", "成功");
        Boolean result = jPushService.send(msg, id, String.valueOf(userId));
        saveMsg(userId, result, id, msg);
    }

    @Async
    public void transferMsg(BigInteger orderId, BigInteger userId, BigDecimal value, String tokenName, Integer transferType, Integer status) {
        String transferTypeStr = transferType == 1 ? "收款" : transferType == 2 ? "提现" : "划账";
        String statusStr = status == 2 ? "成功" : "失败";
        String msg = String.format(MODEL_TRANSFER, value.stripTrailingZeros().toPlainString(), tokenName, transferTypeStr, statusStr);
        Boolean result = jPushService.send(msg, orderId, String.valueOf(userId));
        saveMsg(userId, result, orderId, msg);
    }

    @Async
    public void sendProject(BigInteger userId, BigInteger projectId, BigInteger orderId, Integer status, Integer projectStatus, String tokenName, String projectName, BigDecimal value) {
        Map<String, String> extra = new HashMap<>(1);
        extra.put("projectId", projectId.toString());
        String statusStr = status == 9 ? "失败" : "成功";
        //项目状态0即将开始 1进行中 2已结束 3发币中 9取消
        String projectStatusStr = projectStatus == 0 || projectStatus == 1 ? "预约" : "众筹";
        String valueStr = status == 9 ? "" : ":" + value.stripTrailingZeros().toPlainString() + " " + tokenName;
        String msg = String.format(MODEL_PROJECT, projectStatusStr, statusStr, tokenName, projectName, valueStr);
        Boolean result = jPushService.send(msg, orderId, String.valueOf(userId));
        saveMsg(userId, result, orderId, msg);
    }

    @Async
    public void sendPublish(BigInteger projectId, String projectName, Long time, String tokenName, List<AppOrder> list) {
        String dataStr = new SimpleDateFormat("yyyy月MM日").format(new Date(time));
        String msg = String.format(MODEL_PUBLISH, tokenName, projectName, dataStr);
        Boolean result = jPushService.sendTag(msg, projectId);
        saveMsgByProjectId(projectId, result, msg, list);
    }

    private void saveMsgByProjectId(BigInteger projectId, Boolean result, String msg, List<AppOrder> list) {
        list.forEach(obj -> {
            saveMsg(obj.getUserId(), result, obj.getId(), msg);
        });
    }

    @Async
    public void sendTrade(BigInteger orderId, BigInteger userId, String pairName, Integer transferType, BigDecimal value, String tokenName) {
        String transferTypeStr = transferType == 1 ? "购买" : "出售";
        String msg = String.format(MODEL_TRADE, pairName, transferTypeStr, value.stripTrailingZeros().toPlainString(), tokenName);
        Boolean result = jPushService.send(msg, orderId, String.valueOf(userId));
        saveMsg(userId, result, orderId, msg);
    }

    private AppMessage saveMsg(BigInteger userId, Boolean result, BigInteger orderId, String msg) {
        Long time = System.currentTimeMillis();
        AppMessage appMessage = new AppMessage();
        appMessage.setIsRead(0);
        appMessage.setUserId(userId);
        appMessage.setUpdatedAt(time);
        appMessage.setStatus(result ? 1 : 0);
        appMessage.setSendFlag(1);
        appMessage.setPushTime(time);
        appMessage.setMessageType(1);
        appMessage.setCreatedAt(time);
        appMessage.setContentType("ORDER");
        appMessage.setContentId(orderId);
        appMessage.setMessage(msg);
        appMessageMapper.insert(appMessage);
        return appMessage;
    }

    public Integer update(AppMessage appMessage) {
        return appMessageMapper.updateByPrimaryKey(appMessage);
    }

    public List<AppMessage> findByCondition(Condition condition) {
        return appMessageMapper.selectByCondition(condition);
    }

    public void transferMsg(BigInteger id, BigInteger userId, String message, Boolean send) {
        if (send) {
            Boolean result = jPushService.send(message, id, String.valueOf(userId));
            saveMsg(userId, result, id, message);
        } else {
            saveMsg(userId, false, id, message);
        }
    }

}