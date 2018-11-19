package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.MyOrderVO;
import com.mvc.cryptovault.common.bean.vo.OrderVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 只缓存前几页数据，后几页数据短生命周期缓存和搜索引擎结合.暂时直接走数据库
 */
@Service
public class AppUserTransactionService extends AbstractService<AppUserTransaction> implements BaseService<AppUserTransaction> {
    private final String KEY_PREFIX = "AppUserTransaction".toUpperCase() + "_";

    @Autowired
    CommonPairService commonPairService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    CommonTokenControlService commonTokenControlService;

    public List<OrderVO> getTransactions(OrderDTO dto) {
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "parent_id = ", 0);
        ConditionUtil.andCondition(criteria, "status = ", 0);
        ConditionUtil.andCondition(criteria, "transaction_type = ", dto.getTransactionType());
        ConditionUtil.andCondition(criteria, "pair_id =", dto.getPairId());
        PageHelper.startPage(1, dto.getPageSize());
        if (1 == dto.getTransactionType()) {
            PageHelper.orderBy("price asc,id desc");
        } else {
            PageHelper.orderBy("price desc,id desc");
        }
        if (dto.getType() == 0 && null != dto.getId()) {
            ConditionUtil.andCondition(criteria, "id > ", dto.getId());
        } else if (dto.getType() == 1 && null != dto.getId()) {
            ConditionUtil.andCondition(criteria, "id < ", dto.getId());
        }
        List<AppUserTransaction> list = findByCondition(condition);
        List<OrderVO> result = new ArrayList<>(list.size());
        list.forEach(obj -> {
            OrderVO vo = new OrderVO();
            AppUser user = appUserService.findById(obj.getUserId());
            vo.setHeadImage(user.getHeadImage());
            vo.setLimitValue(obj.getValue().subtract(obj.getSuccessValue()));
            vo.setNickname(user.getNickname());
            vo.setTotal(obj.getValue());
            vo.setId(obj.getId());
            result.add(vo);
        });
        return result;
    }

    public List<MyOrderVO> getUserTransactions(BigInteger userId, MyTransactionDTO dto) {
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "pair_id = ", dto.getPairId());
        ConditionUtil.andCondition(criteria, "status = ", dto.getStatus());
        ConditionUtil.andCondition(criteria, "transaction_type = ", dto.getTransactionType());
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        PageHelper.startPage(1, dto.getPageSize());
        PageHelper.orderBy("id desc");
        if (dto.getType() == 0 && null != dto.getId()) {
            ConditionUtil.andCondition(criteria, "id > ", dto.getId());
        } else if (dto.getType() == 1 && null != dto.getId()) {
            ConditionUtil.andCondition(criteria, "id < ", dto.getId());
        }
        List<AppUserTransaction> list = findByCondition(condition);
        List<MyOrderVO> result = new ArrayList<>(list.size());
        list.forEach(obj -> {
            MyOrderVO vo = new MyOrderVO();
            AppUser user = appUserService.findById(obj.getTargetUserId());
            BeanUtils.copyProperties(obj, vo);
            vo.setDeal(obj.getValue());
            vo.setNickname(null == user ? "" : user.getNickname());
            result.add(vo);
        });
        return result;
    }

    //TODO 步骤较多,异步化
    public void buy(BigInteger userId, TransactionBuyDTO dto) {
        CommonPair pair = commonPairService.findById(dto.getPairId());
        CommonTokenPrice tokenPrice = commonTokenPriceService.findById(pair.getTokenId());
        //校验余额是否足够
        checkBalance(userId, dto, pair);
        //校验浮动范围是否正确
        checkPrice(dto, pair, tokenPrice);
        Long time = System.currentTimeMillis();
        AppUserTransaction transaction = new AppUserTransaction();
        transaction.setUpdatedAt(time);
        transaction.setCreatedAt(time);
        Long id = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
        transaction.setOrderNumber("P" + String.format("%09d", id));
        transaction.setPairId(dto.getPairId());
        transaction.setPrice(dto.getPrice());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setUserId(userId);
        transaction.setValue(dto.getValue());
        if (dto.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
            //购买挂单时扣除基础货币价格*购买数量
            appUserBalanceService.updateBalance(userId, pair.getBaseTokenId(), BigDecimal.ZERO.subtract(dto.getValue().multiply(dto.getPrice())));
        } else {
            //出售时扣除目标货币数量
            appUserBalanceService.updateBalance(userId, pair.getTokenId(), BigDecimal.ZERO.subtract(dto.getValue()));
        }
        if (null == dto.getId() || dto.getId().equals(BigInteger.ZERO)) {
            //普通挂单
            transaction.setStatus(0);
            transaction.setParentId(BigInteger.ZERO);
            transaction.setSuccessValue(BigDecimal.ZERO);
            transaction.setTargetUserId(BigInteger.ZERO);
            save(transaction);
        } else {
            Long targetId = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
            var targetTransaction = findById(dto.getId());
            //校验可购买量是否足够
            checkValue(dto, targetTransaction);
            //修改主单购买信息
            targetTransaction.setSuccessValue(transaction.getSuccessValue().add(dto.getValue()));
            targetTransaction.setUpdatedAt(time);
            if (transaction.getSuccessValue().equals(transaction.getValue())) {
                transaction.setStatus(1);
            }
            update(targetTransaction);
            //生成用户主动交易记录
            transaction.setStatus(1);
            transaction.setParentId(BigInteger.ZERO);
            transaction.setSuccessValue(dto.getValue());
            transaction.setTargetUserId(targetTransaction.getUserId());
            save(transaction);
            //生成目标用户交易记录
            var targetSubTransaction = new AppUserTransaction();
            BeanUtils.copyProperties(transaction, targetSubTransaction);
            targetSubTransaction.setId(null);
            targetSubTransaction.setUserId(targetTransaction.getUserId());
            targetSubTransaction.setTargetUserId(userId);
            targetSubTransaction.setParentId(targetTransaction.getId());
            targetSubTransaction.setOrderNumber("P" + String.format("%09d", targetId));
            targetSubTransaction.setTransactionType(dto.getTransactionType().equals(1) ? 2 : 1);
            save(targetSubTransaction);
            //成交后根据买卖分类更新对应余额
            if (dto.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
                //购买成功时添加币种余额
                appUserBalanceService.updateBalance(userId, pair.getTokenId(), dto.getValue());
                appUserBalanceService.updateBalance(targetTransaction.getUserId(), pair.getBaseTokenId(), dto.getValue().multiply(dto.getPrice()));
            } else {
                //出售时添加基础货币余额
                appUserBalanceService.updateBalance(userId, pair.getBaseTokenId(), dto.getValue().multiply(dto.getPrice()));
                appUserBalanceService.updateBalance(targetTransaction.getUserId(), pair.getBaseTokenId(), dto.getValue());
            }
        }
    }

    private void checkBalance(BigInteger userId, TransactionBuyDTO dto, CommonPair pair) {
        CommonTokenPrice tokenPrice = commonTokenPriceService.findById(pair.getBaseTokenId());
        BigDecimal balance = appUserBalanceService.getBalanceByTokenId(userId, pair.getBaseTokenId());
        Assert.isTrue(dto.getValue().subtract(tokenPrice.getTokenPrice()).compareTo(balance) <= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
    }

    private void checkPrice(TransactionBuyDTO dto, CommonPair pair, CommonTokenPrice tokenPrice) {
        CommonTokenControl tokenControl = commonTokenControlService.findById(pair.getTokenId());
        Float floatValue = dto.getPrice().subtract(tokenPrice.getTokenPrice()).divide(dto.getValue()).floatValue();
        if (dto.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
            Assert.isTrue(tokenControl.getBuyMin() <= floatValue && tokenControl.getBuyMax() >= floatValue, MessageConstants.getMsg("APP_TRANSACTION_LIMIT_OVER"));
        } else {
            Assert.isTrue(tokenControl.getSellMin() <= floatValue && tokenControl.getSellMax() >= floatValue, MessageConstants.getMsg("APP_TRANSACTION_LIMIT_OVER"));
        }
    }

    private void checkValue(TransactionBuyDTO dto, AppUserTransaction targetTransaction) {
        Assert.isTrue(dto.getValue().add(targetTransaction.getSuccessValue()).compareTo(targetTransaction.getValue()) <= 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
    }

    public void cancel(BigInteger userId, BigInteger id) {
        AppUserTransaction trans = findById(id);
        if(trans.getStatus().equals(BusinessConstant.STATUS_CANCEL)){
            return;
        }
         trans = findById(id);
        trans.setStatus(BusinessConstant.STATUS_CANCEL);
        trans.setUpdatedAt(System.currentTimeMillis());
        update(trans);
        trans = findById(id);
        CommonPair pair = commonPairService.findById(trans.getPairId());
        //还原未购买的余额
        if (trans.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
            //购买
            appUserBalanceService.updateBalance(userId, pair.getBaseTokenId(), (trans.getValue().subtract(trans.getSuccessValue())).multiply(trans.getPrice()));
        } else {
            //出售
            appUserBalanceService.updateBalance(userId, pair.getTokenId(), trans.getValue().subtract(trans.getSuccessValue()));
        }
    }

}