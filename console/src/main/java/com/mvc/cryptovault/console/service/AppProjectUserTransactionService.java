package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppProjectUserTransaction;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.PurchaseVO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectOrderDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectOrderVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.AppProjectUserTransactionMapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

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
    @Autowired
    CommonPairService commonPairService;
    @Autowired
    CommonTokenService commonTokenService;

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
        putAll(userId, false);
        Long time = System.currentTimeMillis();
        AppProject project = buyCheck(userId, projectId, dto);
        AppProjectUserTransaction appProjectUserTransaction = saveAppProjectUserTransaction(userId, projectId, dto, time, project);
        updateCache(userId, dto, project, appProjectUserTransaction);
        appProjectUserTransaction.setResult(0);
        appOrderService.saveOrder(appProjectUserTransaction, project);
        return true;
    }

    private void updateCache(BigInteger userId, ProjectBuyDTO dto, AppProject project, AppProjectUserTransaction appProjectUserTransaction) {
        appUserBalanceService.updateBalance(userId, project.getBaseTokenId(), BigDecimal.ZERO.subtract(appProjectUserTransaction.getPayed()));
        //TODO 异步发送推送,修改余额,生成统一订单,添加到getReservation缓存列表
        String balanceKey = "AppProjectUserTransaction".toUpperCase() + "_BALANCE_" + userId;
        redisTemplate.boundHashOps(balanceKey).increment(String.valueOf(appProjectUserTransaction.getProjectId()), Double.valueOf(String.valueOf(dto.getValue())));
        String key = "AppProjectUserTransaction".toUpperCase() + "_INDEX_" + userId;
        String listKey = "AppProjectUserTransaction".toUpperCase() + "_USER_" + userId;
        redisTemplate.boundHashOps(key).put(String.valueOf(appProjectUserTransaction.getId()), String.valueOf(appProjectUserTransaction.getIndex()));
        redisTemplate.delete(listKey);
        putAll(userId, true);
    }

    @NotNull
    private AppProjectUserTransaction saveAppProjectUserTransaction(BigInteger userId, BigInteger projectId, ProjectBuyDTO dto, Long time, AppProject project) {
        AppProjectUserTransaction appProjectUserTransaction = new AppProjectUserTransaction();
        appProjectUserTransaction.setCreatedAt(time);
        appProjectUserTransaction.setUpdatedAt(time);
        appProjectUserTransaction.setPairId(project.getPairId());
        appProjectUserTransaction.setProjectId(projectId);
        AppProjectUserTransaction temp = new AppProjectUserTransaction();
        temp.setUserId(userId);
        appProjectUserTransaction.setIndex(appProjectUserTransactionMapper.selectCount(temp));
        appProjectUserTransaction.setUserId(userId);
        appProjectUserTransaction.setResult(BusinessConstant.APP_PROJECT_STATUS_WAIT);
        appProjectUserTransaction.setValue(dto.getValue());
        appProjectUserTransaction.setProjectOrderNumber(getOrderNumber());
        appProjectUserTransaction.setSuccessPayed(BigDecimal.ZERO);
        appProjectUserTransaction.setSuccessValue(BigDecimal.ZERO);
        //花费金额=购买数量*货币比值
        BigDecimal balanceCost = dto.getValue().multiply(BigDecimal.valueOf(project.getRatio()));
        appProjectUserTransaction.setPayed(balanceCost);
        appProjectUserTransactionMapper.insert(appProjectUserTransaction);
        return appProjectUserTransaction;
    }

    private AppProject buyCheck(BigInteger userId, BigInteger projectId, ProjectBuyDTO dto) {
        AppUser user = appUserService.findById(userId);
        Assert.isTrue(user.getTransactionPassword().equalsIgnoreCase(dto.getPassword()), MessageConstants.getMsg("USER_TRANS_PASS_WRONG"));
        AppProject project = appProjectService.findById(projectId);
        ProjectBuyVO balance = appUserBalanceService.getBalance(userId, project);
        Assert.isTrue(balance.getBalance().compareTo(dto.getValue().multiply(NumberUtils.parseNumber(String.valueOf(project.getRatio()), BigDecimal.class))) >= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
        Assert.isTrue(balance.getLimitValue().subtract(dto.getValue()).compareTo(BigDecimal.ZERO) >= 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        Assert.isTrue(dto.getValue().subtract(balance.getProjectMin()).compareTo(BigDecimal.ZERO) >= 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        return project;
    }

    public List<PurchaseVO> getReservation(BigInteger userId, ReservationDTO reservationDTO) {
        List<AppProjectUserTransaction> transList = null;
        List<PurchaseVO> result = new ArrayList<>(10);
        if (StringUtils.isNotBlank(reservationDTO.getProjectName())) {
            String ids = appProjectService.findIdsByName(reservationDTO.getProjectName());
            if (StringUtils.isBlank(ids)) {
                return new ArrayList<>(0);
            }
            //按项目名搜索不分页
            transList = getTransByProjectIds(userId, ids);
        } else {
            transList = getAppProjectUserTransactionsCache(userId, reservationDTO);
            if (transList == null) return null;
        }
        for (int i = 0; i < transList.size(); i++) {
            PurchaseVO vo = new PurchaseVO();
            AppProjectUserTransaction appProjectUserTransaction = transList.get(i);
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
            vo.setTokenId(project.getTokenId());
            vo.setTokenName(project.getTokenName());
            vo.setBaseTokenName(project.getBaseTokenName());
            vo.setSuccessPayed(appProjectUserTransaction.getSuccessPayed());
            vo.setSuccessValue(appProjectUserTransaction.getSuccessValue());
            vo.setPublishAt(project.getPublishAt());
            result.add(vo);
        }
        return result;
    }

    private List<AppProjectUserTransaction> getTransByProjectIds(BigInteger userId, String ids) {
        Condition condition = new Condition(AppProjectUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.orderBy("id desc");
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        ConditionUtil.andCondition(criteria, String.format("project_id in (%s)", ids));
        return findByCondition(condition);
    }

    @Nullable
    private List<AppProjectUserTransaction> getAppProjectUserTransactionsCache(BigInteger userId, ReservationDTO reservationDTO) {
        PageHelper.startPage(1, reservationDTO.getPageSize(), "id desc");
        Condition condition = new Condition(AppProjectUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        if(null != reservationDTO.getId() && !reservationDTO.getId().equals(BigInteger.ZERO)){
            ConditionUtil.andCondition(criteria, "id < ", reservationDTO.getId());
        }
        return findByCondition(condition);
    }

    private Integer getIndex(BigInteger id, BigInteger userId) {
        String key = "AppProjectUserTransaction".toUpperCase() + "_INDEX_" + userId;
        String index = (String) redisTemplate.boundHashOps(key).get(String.valueOf(id));
        if (StringUtils.isBlank(index)) {
            return null;
        }
        return NumberUtils.parseNumber(index, Integer.class);
    }

    public void putAll(BigInteger userId, Boolean flag) {
        String key = "AppProjectUserTransaction".toUpperCase() + "_INDEX_" + userId;
        if (!redisTemplate.hasKey(key) || flag) {
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

    public PageInfo<DProjectOrderVO> findOrders(DProjectOrderDTO dto) {
        AppUser user = StringUtils.isBlank(dto.getCellphone()) ? null : appUserService.findOneBy("cellphone", dto.getCellphone());
        AppProject project = StringUtils.isBlank(dto.getProjectName()) ? null : appProjectService.findOneBy("projectName", dto.getProjectName());
        Boolean flag = StringUtils.isNotBlank(dto.getCellphone()) && null == user || StringUtils.isNotBlank(dto.getProjectName()) && null == project;
        if (flag) {
            return new PageInfo<>();
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        Condition condition = new Condition(AppProjectUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", null == user ? null : user.getId());
        ConditionUtil.andCondition(criteria, "project_id = ", null == project ? null : project.getId());
        ConditionUtil.andCondition(criteria, "created_at >= ", dto.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", dto.getCreatedStopAt());
        ConditionUtil.andCondition(criteria, "result = ", dto.getStatus());
        List<AppProjectUserTransaction> list = findByCondition(condition);
        List<DProjectOrderVO> vos = new ArrayList<>(list.size());
        for (AppProjectUserTransaction transaction : list) {
            DProjectOrderVO vo = new DProjectOrderVO();
            BeanUtils.copyProperties(transaction, vo);
            user = appUserService.findById(transaction.getUserId());
            project = appProjectService.findById(transaction.getProjectId());
            vo.setStatus(transaction.getResult());
            vo.setBaseTokenId(project.getBaseTokenId());
            vo.setBaseTokenName(project.getBaseTokenName());
            vo.setCellphone(user.getCellphone());
            vo.setPayed(transaction.getPayed());
            vo.setProjectName(project.getProjectName());
            vo.setTokenId(project.getTokenId());
            vo.setProjectStatus(project.getStatus());
            vo.setOrderNumber(transaction.getProjectOrderNumber());
            vo.setTokenName(project.getTokenName());
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public Boolean existTrans(BigInteger id) {
        AppProjectUserTransaction userTransaction = appProjectUserTransactionMapper.existTrans(id);
        if (null != userTransaction) {
            return true;
        }
        return false;
    }

    public void updatePartake(ImportPartake partake, AppProject appProject) {
        appProject.setStatus(2);
        AppProjectUserTransaction appProjectUserTransaction = new AppProjectUserTransaction();
        appProjectUserTransaction.setProjectId(partake.getProjectId());
        appProjectUserTransaction.setResult(0);
        appProjectUserTransaction.setUserId(partake.getUserId());
        List<AppProjectUserTransaction> list = findByEntity(appProjectUserTransaction);
        BigDecimal value = partake.getValue();
        for (AppProjectUserTransaction transaction : list) {
            if (value.equals(BigDecimal.ZERO)) {
                break;
            }
            if (value.compareTo(transaction.getValue()) <= 0) {
                transaction.setSuccessValue(value);
                transaction.setSuccessPayed(value.multiply(new BigDecimal(appProject.getRatio())));
                transaction.setResult(1);
                appProjectUserTransactionMapper.updateSuccess(transaction, System.currentTimeMillis());
                //添加到统一订单并添加推送
                if (transaction.getSuccessValue().compareTo(transaction.getValue()) != 0) {
                    //没有全额成功,退还剩余费用
                    appUserBalanceService.updateBalance(transaction.getUserId(), appProject.getBaseTokenId(), transaction.getPayed().subtract(transaction.getSuccessPayed()));
                    appOrderService.saveOrderProject(transaction, appProject);
                    appOrderService.setOrderReturn(transaction, appProject);
                } else {
                    appOrderService.saveOrderProject(transaction, appProject);
                }
                break;
            } else {
                //单笔订单购买数量小于总购买数量,需要分多次处理
                value = value.subtract(transaction.getValue());
                transaction.setSuccessValue(transaction.getValue());
                transaction.setResult(1);
                transaction.setSuccessPayed(transaction.getValue().multiply(new BigDecimal(appProject.getRatio())));
                appProjectUserTransactionMapper.updateSuccess(transaction, System.currentTimeMillis());
                appOrderService.saveOrderProject(transaction, appProject);
            }
        }

    }

    public void updateFailPartake(BigInteger projectId, AppProject project) {
        AppProjectUserTransaction transaction = new AppProjectUserTransaction();
        transaction.setProjectId(projectId);
        transaction.setResult(0);
        List<AppProjectUserTransaction> list = findByEntity(transaction);
        list.stream().forEach(trans -> {
            String listKey = "AppProjectUserTransaction".toUpperCase() + "_USER_" + trans.getUserId();
            trans.setResult(9);
            appProjectUserTransactionMapper.updateSuccess(trans, System.currentTimeMillis());
            appUserBalanceService.updateBalance(trans.getUserId(), project.getBaseTokenId(), trans.getPayed().subtract(trans.getSuccessPayed()));
            trans.setSuccessValue(trans.getValue());
            trans.setSuccessPayed(trans.getPayed());
            appOrderService.saveOrderProject(trans, project);
            redisTemplate.delete(listKey);
            putAll(trans.getUserId(), true);
        });

    }
}