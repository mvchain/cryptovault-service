package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.PurchaseVO;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.AppProjectUserTransactionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppProjectUserTransactionService extends AbstractService<AppProjectUserTransaction> implements BaseService<AppProjectUserTransaction> {

    @Autowired
    AppProjectUserTransactionMapper appProjectUserTransactionMapper;
    @Autowired
    AppProjectService appProjectService;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    AppOrderDetailService appOrderDetailService;

    public BigDecimal getUserBuyTotal(BigInteger userId, BigInteger project) {
        String key = "AppProjectUserTransaction".toUpperCase() + "_BALANCE_" + userId;
        if (redisTemplate.hasKey(key)) {
            String balance = (String) redisTemplate.boundHashOps(key).get(String.valueOf(project));
            if (StringUtils.isNotBlank(balance) && !"null".equals(balance)) {
                return NumberUtils.parseNumber(balance, BigDecimal.class);
            }
        }
        BigDecimal result = null;
        BigDecimal sum = appProjectUserTransactionMapper.sum(userId, project);
        if (null == sum) {
            result = BigDecimal.ZERO;
        } else {
            result = sum;
        }
        //余额记录永久保存
        redisTemplate.boundHashOps(key).put(String.valueOf(project), String.valueOf(result));
        return result;
    }

    public Boolean buy(BigInteger userId, BigInteger projectId, ProjectBuyDTO dto) {
        putAll(userId);
        Long time = System.currentTimeMillis();
        AppUser user = appUserService.findById(userId);
        Assert.isTrue(user.getTransactionPassword().equalsIgnoreCase(dto.getPassword()), MessageConstants.getMsg("USER_TRANS_PASS_WRONG"));
        AppProject project = appProjectService.findById(projectId);
        ProjectBuyVO balance = appUserBalanceService.getBalance(userId, project);
        Assert.isTrue(balance.getBalance().compareTo(dto.getValue()) >= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
        Assert.isTrue(balance.getLimitValue().subtract(dto.getValue()).compareTo(BigDecimal.ZERO) > 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        Long id = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
        AppProjectUserTransaction appProjectUserTransaction = new AppProjectUserTransaction();
        appProjectUserTransaction.setCreatedAt(time);
        appProjectUserTransaction.setUpdatedAt(time);
        appProjectUserTransaction.setPairId(project.getPairId());
        appProjectUserTransaction.setProjectId(projectId);
        AppProjectUserTransaction temp = new AppProjectUserTransaction();
        appProjectUserTransaction.setIndex(appProjectUserTransactionMapper.selectCount(temp));
        appProjectUserTransaction.setUserId(userId);
        appProjectUserTransaction.setResult(BusinessConstant.APP_PROJECT_STATUS_WAIT);
        appProjectUserTransaction.setValue(dto.getValue());
        appProjectUserTransaction.setProjectOrderNumber("P" + String.format("%09d", id));
        //花费金额=购买数量*货币比值
        BigDecimal balanceCost = dto.getValue().multiply(BigDecimal.valueOf(project.getRatio()));
        appUserBalanceService.updateBalance(userId, project.getBaseTokenId(), BigDecimal.ZERO.subtract(balanceCost));
        appProjectUserTransactionMapper.insert(appProjectUserTransaction);
        //TODO 异步发送推送,修改余额,生成统一订单,添加到getReservation缓存列表
        String balanceKey = "AppProjectUserTransaction".toUpperCase() + "_BALANCE_" + userId;
        redisTemplate.boundHashOps(balanceKey).increment(String.valueOf(appProjectUserTransaction.getProjectId()), Double.valueOf(String.valueOf(dto.getValue())));
        String key = "AppProjectUserTransaction".toUpperCase() + "_INDEX_" + userId;
        String listKey = "AppProjectUserTransaction".toUpperCase() + "_USER_" + userId;
        redisTemplate.boundHashOps(key).put(String.valueOf(appProjectUserTransaction.getId()), String.valueOf(appProjectUserTransaction.getIndex()));
        redisTemplate.boundListOps(listKey).rightPush(JSON.toJSONString(appProjectUserTransaction));
        AppOrder appOrder = new AppOrder();
        appOrder.setClassify(0);
        appOrder.setCreatedAt(time);
        appOrder.setUpdatedAt(time);
        appOrder.setFromAddress("");
        appOrder.setHash("");
        appOrder.setOrderContentId(appProjectUserTransaction.getId());
        appOrder.setOrderContentName("BLOCK");
        appOrder.setOrderNumber(appProjectUserTransaction.getProjectOrderNumber());
        appOrder.setValue(balanceCost);
        appOrder.setUserId(userId);
        appOrder.setTokenId(project.getBaseTokenId());
        appOrder.setStatus(0);
        appOrder.setOrderType(1);
        appOrderService.save(appOrder);
        AppOrderDetail detail = new AppOrderDetail();
        detail.setCreatedAt(time);
        detail.setUpdatedAt(time);
        detail.setFee(BigDecimal.ZERO);
        detail.setFromAddress("");
        detail.setHash("");
        detail.setToAddress("");
        detail.setOrderId(appOrder.getId());
        detail.setValue(dto.getValue());
        appOrderDetailService.save(detail);
        return true;
    }

    public List<PurchaseVO> getReservation(BigInteger userId, ReservationDTO reservationDTO) {
        String listKey = "AppProjectUserTransaction".toUpperCase() + "_USER_" + userId;
        putAll(userId);
        List<String> list = null;
        if (reservationDTO.getId() == null || reservationDTO.getId().equals(BigInteger.ZERO)) {
            list = redisTemplate.boundListOps(listKey).range(0, reservationDTO.getPageSize());
        } else {
            Integer index = getIndex(reservationDTO.getId(), userId);
            if (null == index) {
                list = null;
            } else if (reservationDTO.getType().equals(BusinessConstant.SEARCH_DIRECTION_UP)) {
                //上拉则最新数据
                list = redisTemplate.boundListOps(listKey).range(index + 1, index + reservationDTO.getPageSize());
            } else {
                list = redisTemplate.boundListOps(listKey).range(index - reservationDTO.getPageSize(), index - 1);
            }
        }
        if (null == list) {
            return null;
        }
        List<PurchaseVO> result = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            PurchaseVO vo = new PurchaseVO();
            AppProjectUserTransaction appProjectUserTransaction = JSON.parseObject(list.get(i), AppProjectUserTransaction.class);
            AppProject project = appProjectService.findById(appProjectUserTransaction.getProjectId());
            vo.setCreatedAt(appProjectUserTransaction.getCreatedAt());
            vo.setId(appProjectUserTransaction.getId());
            vo.setPrice(appProjectUserTransaction.getValue().multiply(new BigDecimal(project.getRatio())));
            vo.setProjectId(appProjectUserTransaction.getProjectId());
            vo.setProjectName(project.getProjectName());
            vo.setProjectOrderId(appProjectUserTransaction.getProjectOrderNumber());
            vo.setValue(appProjectUserTransaction.getValue());
            vo.setStopAt(project.getStopAt());
            vo.setReservationType(appProjectUserTransaction.getResult());
            vo.setReleaseValue(project.getReleaseValue());
            vo.setRatio(project.getRatio());
            vo.setPublishAt(project.getPublishAt());
            result.add(vo);
        }
        return result;
    }

    private Integer getIndex(BigInteger id, BigInteger userId) {
        String key = "AppProjectUserTransaction".toUpperCase() + "_INDEX_" + userId;
        String index = (String) redisTemplate.boundHashOps(key).get(String.valueOf(id));
        if (StringUtils.isBlank(index)) {
            return null;
        }
        return NumberUtils.parseNumber(index, Integer.class);
    }

    private void putAll(BigInteger userId) {
        String key = "AppProjectUserTransaction".toUpperCase() + "_INDEX_" + userId;
        if (!redisTemplate.hasKey(key)) {
            String listKey = "AppProjectUserTransaction".toUpperCase() + "_USER_" + userId;
            List<AppProjectUserTransaction> list = findBy("userId", userId);
            if (list.size() == 0) {
                redisTemplate.delete(listKey);
                redisTemplate.boundHashOps(key).put("0", "");
            }
            redisTemplate.delete(listKey);
            list.forEach(obj -> {
                redisTemplate.boundHashOps(key).put(String.valueOf(obj.getId()), String.valueOf(obj.getIndex()));
                redisTemplate.boundListOps(listKey).rightPush(JSON.toJSONString(obj));
            });
        }
    }
}