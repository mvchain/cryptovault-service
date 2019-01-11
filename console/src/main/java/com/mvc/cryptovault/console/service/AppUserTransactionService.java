package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.MyOrderVO;
import com.mvc.cryptovault.common.bean.vo.OrderVO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.OverTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.OverTransactionVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.AppUserTransactionMapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    TokenVolumeService tokenVolumeService;
    @Autowired
    AppUserTransactionMapper appUserTransactionMapper;
    @Autowired
    AppProjectService appProjectService;

    public List<OrderVO> getTransactions(OrderDTO dto) {
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "parent_id = ", 0);
        ConditionUtil.andCondition(criteria, "status = ", 0);
        ConditionUtil.andCondition(criteria, "transaction_type = ", dto.getTransactionType());
        ConditionUtil.andCondition(criteria, "pair_id =", dto.getPairId());
        PageHelper.startPage(1, dto.getPageSize());
        if (2 == dto.getTransactionType()) {
            PageHelper.orderBy("price asc,id asc");
        } else {
            PageHelper.orderBy("price desc,id desc");
        }
        if (dto.getType() == 0 && null != dto.getId()) {
            ConditionUtil.andCondition(criteria, "id > ", dto.getId());
        } else if (dto.getType() == 1 && null != dto.getId()) {
            AppUserTransaction trans = appUserTransactionMapper.selectByPrimaryKey(dto.getId());
            if(null != trans){
                if (2 == dto.getTransactionType()) {
                    ConditionUtil.andCondition(criteria, String.format("CONCAT(price, id) > '%s'", trans.getPrice() + "" + String.format("%012d", trans.getId())));
                } else {
                    ConditionUtil.andCondition(criteria, String.format("CONCAT(price, id) < '%s'", trans.getPrice() + "" + String.format("%012d", trans.getId())));
                }
            }
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
            vo.setTransactionType(obj.getTransactionType());
            vo.setId(obj.getId());
            vo.setPrice(obj.getPrice());
            result.add(vo);
        });
        return result;
    }

    public List<MyOrderVO> getUserTransactions(BigInteger userId, MyTransactionDTO dto) {
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "pair_id = ", dto.getPairId());
        if (null == dto.getStatus()) {
            ConditionUtil.andCondition(criteria, "status in (0, 1)");
        } else {
            ConditionUtil.andCondition(criteria, "status = ", dto.getStatus());
        }
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
        dto.setId(BigInteger.ZERO.equals(dto.getId()) ? null : dto.getId());
        CommonPair pair = commonPairService.findById(dto.getPairId());
        CommonTokenPrice tokenPrice = commonTokenPriceService.findById(pair.getTokenId());
        CommonTokenControl token = commonTokenControlService.findById(pair.getTokenId());
        AppUserTransaction targetTransaction = null;
        //校验开关是否开启
        Assert.isTrue(null != token && token.getTransactionStatus() == 1, MessageConstants.getMsg("TRANS_STATUS_CLOSE"));
        //校验余额是否足够
        checkBalance(userId, dto, pair);
        //校验浮动范围是否正确
        checkPrice(dto, pair, tokenPrice);
        if (null != dto.getId() && !dto.getId().equals(BigInteger.ZERO)) {
            //校验订单信息是否输入错误
            targetTransaction = mapper.selectByPrimaryKey(dto.getId());
            checkDto(targetTransaction, dto);
            //校验可购买量是否足够
            checkValue(dto, targetTransaction);
        }
        saveAll(userId, dto, targetTransaction, pair);
    }

    private void checkDto(AppUserTransaction targetTransaction, TransactionBuyDTO dto) {
        Assert.isTrue(!targetTransaction.getTransactionType().equals(dto.getTransactionType()), MessageConstants.getMsg("TRANS_MSG_ERROR"));
        Assert.isTrue(dto.getPrice().compareTo(targetTransaction.getPrice()) == 0, MessageConstants.getMsg("TRANS_MSG_ERROR"));
    }

    public void saveAll(BigInteger userId, TransactionBuyDTO dto, AppUserTransaction targetTransaction, CommonPair pair) {
        Long time = System.currentTimeMillis();
        AppUserTransaction transaction = getAppUserTransaction(userId, dto, time);
        updateBalance(userId, dto, pair);
        if (null == dto.getId() || dto.getId().equals(BigInteger.ZERO)) {
            //普通挂单
            saveTopTransaction(transaction);
        } else {
            saveChildTransaction(userId, dto, pair, time, transaction, targetTransaction);
            //记录成交量
            TokenVolume tokenVolume = new TokenVolume();
            tokenVolume.setCreatedAt(System.currentTimeMillis());
            tokenVolume.setValue(dto.getValue());
            tokenVolume.setTokenId(pair.getTokenId());
            tokenVolume.setUsed(0);
            if (pair.getTokenId().compareTo(BusinessConstant.BASE_TOKEN_ID_USDT) <= 0) {
                tokenVolume.setUsed(1);
            }
            tokenVolumeService.save(tokenVolume);
        }

    }

    private void saveChildTransaction(BigInteger userId, TransactionBuyDTO dto, CommonPair pair, Long time, AppUserTransaction transaction, AppUserTransaction targetTransaction) {
        //修改主单购买信息
        Integer result = appUserTransactionMapper.updateValue(dto.getId(), dto.getValue(), System.currentTimeMillis());
        Assert.isTrue(result > 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        //生成用户主动交易记录
        transaction.setStatus(1);
        transaction.setParentId(targetTransaction.getId());
        transaction.setSuccessValue(dto.getValue());
        transaction.setTargetUserId(targetTransaction.getUserId());
        transaction.setSelfOrder(1);
        save(transaction);
        appOrderService.saveOrder(transaction, pair);
        //生成目标用户交易记录
        var targetSubTransaction = new AppUserTransaction();
        BeanUtils.copyProperties(transaction, targetSubTransaction);
        targetSubTransaction.setId(null);
        targetSubTransaction.setUserId(targetTransaction.getUserId());
        targetSubTransaction.setTargetUserId(userId);
        targetSubTransaction.setParentId(targetTransaction.getId());
        targetSubTransaction.setOrderNumber(getOrderNumber());
        targetSubTransaction.setTransactionType(dto.getTransactionType().equals(1) ? 2 : 1);
        targetSubTransaction.setSelfOrder(0);
        save(targetSubTransaction);
        appOrderService.saveOrder(targetSubTransaction, pair);
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
        updateCache(dto.getId());
    }

    private void saveTopTransaction(AppUserTransaction transaction) {
        transaction.setStatus(0);
        transaction.setParentId(BigInteger.ZERO);
        transaction.setSuccessValue(BigDecimal.ZERO);
        transaction.setTargetUserId(BigInteger.ZERO);
        transaction.setSelfOrder(1);
        save(transaction);
    }

    private void updateBalance(BigInteger userId, TransactionBuyDTO dto, CommonPair pair) {
        if (dto.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
            //购买挂单时扣除基础货币价格*购买数量
            appUserBalanceService.updateBalance(userId, pair.getBaseTokenId(), BigDecimal.ZERO.subtract(dto.getValue().multiply(dto.getPrice())));
        } else {
            //出售时扣除目标货币数量
            appUserBalanceService.updateBalance(userId, pair.getTokenId(), BigDecimal.ZERO.subtract(dto.getValue()));
        }
    }

    @NotNull
    private AppUserTransaction getAppUserTransaction(BigInteger userId, TransactionBuyDTO dto, Long time) {
        AppUserTransaction transaction = new AppUserTransaction();
        transaction.setUpdatedAt(time);
        transaction.setCreatedAt(time);
        transaction.setOrderNumber(getOrderNumber());
        transaction.setPairId(dto.getPairId());
        transaction.setPrice(dto.getPrice());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setUserId(userId);
        transaction.setValue(dto.getValue());
        return transaction;
    }

    private void checkBalance(BigInteger userId, TransactionBuyDTO dto, CommonPair pair) {
        if (dto.getTransactionType() == 1) {
            //购买，需要校验基础货币余额
            BigDecimal balance = appUserBalanceService.getBalanceByTokenId(userId, pair.getBaseTokenId());
            Assert.isTrue(dto.getValue().multiply(dto.getPrice()).compareTo(balance) <= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
        } else {
            //出售，需要校验目标货币余额
            BigDecimal balance = appUserBalanceService.getBalanceByTokenId(userId, pair.getTokenId());
            Assert.isTrue(dto.getValue().compareTo(balance) <= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
        }

    }

    private void checkPrice(TransactionBuyDTO dto, CommonPair pair, CommonTokenPrice tokenPrice) {
        //直接成交交易不校验浮动范围
        if (dto.getId() != null && !dto.getId().equals(BigInteger.ZERO)) {
            return;
        }
        CommonTokenControl tokenControl = commonTokenControlService.findById(pair.getTokenId());
        Float floatValue = dto.getPrice().subtract(tokenPrice.getTokenPrice()).divide(tokenPrice.getTokenPrice(), 10, RoundingMode.HALF_DOWN).floatValue();
        if (null != tokenControl.getMinLimit() && !tokenControl.getMinLimit().equals(BigDecimal.ZERO)) {
            //如果设置了最小购买数量,需要校验
            Assert.isTrue(dto.getValue().compareTo(tokenControl.getMinLimit()) >= 0, MessageConstants.getMsg("APP_TRANSACTION_MIN_OVER"));
        }
        if (dto.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
            Assert.isTrue(tokenControl.getBuyMin() / 100 <= floatValue && tokenControl.getBuyMax() / 100 >= floatValue, MessageConstants.getMsg("APP_TRANSACTION_LIMIT_OVER"));
        } else {
            Assert.isTrue(tokenControl.getSellMin() / 100 <= floatValue && tokenControl.getSellMax() / 100 >= floatValue, MessageConstants.getMsg("APP_TRANSACTION_LIMIT_OVER"));
        }
    }

    private void checkValue(TransactionBuyDTO dto, AppUserTransaction targetTransaction) {
        Assert.isTrue(dto.getValue().add(targetTransaction.getSuccessValue()).compareTo(targetTransaction.getValue()) <= 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
    }

    public void cancel(BigInteger userId, BigInteger id) {
        AppUserTransaction trans = findById(id);
        if (trans.getStatus().equals(BusinessConstant.STATUS_CANCEL)) {
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

    public PageInfo<DTransactionVO> findTransaction(PageDTO pageDTO, DTransactionDTO dTransactionDTO) {
        AppUser appUser = StringUtils.isBlank(dTransactionDTO.getCellphone()) ? null : appUserService.findOneBy("cellphone", dTransactionDTO.getCellphone());
        if (StringUtils.isNotBlank(dTransactionDTO.getCellphone()) && null == appUser) {
            return new PageInfo<>();
        }
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "pair_id = ", dTransactionDTO.getPairId());
        ConditionUtil.andCondition(criteria, "parent_id = ", BigInteger.ZERO);
        ConditionUtil.andCondition(criteria, "status = ", dTransactionDTO.getStatus());
        ConditionUtil.andCondition(criteria, "transaction_type = ", dTransactionDTO.getTransactionType());
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        ConditionUtil.andCondition(criteria, "order_number = ", dTransactionDTO.getOrderNumber());
        ConditionUtil.andCondition(criteria, "user_id = ", null == appUser ? null : appUser.getId());
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        List<AppUserTransaction> list = findByCondition(condition);
        List<DTransactionVO> vos = new ArrayList<>(list.size());
        PageInfo result = new PageInfo(list);
        for (AppUserTransaction transaction : list) {
            DTransactionVO vo = new DTransactionVO();
            BeanUtils.copyProperties(transaction, vo);
            appUser = appUserService.findById(transaction.getUserId());
            CommonPair pair = commonPairService.findById(transaction.getPairId());
            vo.setCellphone(appUser.getCellphone());
            vo.setDeal(transaction.getSuccessValue());
            vo.setPairName(pair.getPairName());
            vo.setSurplus(transaction.getValue().subtract(transaction.getSuccessValue()));
            vos.add(vo);
        }
        result.setList(vos);
        return result;
    }

    public PageInfo<OverTransactionVO> overList(PageDTO pageDTO, OverTransactionDTO overTransactionDTO) {
        AppUser appUser = StringUtils.isBlank(overTransactionDTO.getCellphone()) ? null : appUserService.findOneBy("cellphone", overTransactionDTO.getCellphone());
        AppUserTransaction transaction = null;
        if (StringUtils.isNotBlank(overTransactionDTO.getParentOrderNumber())) {
            AppUserTransaction trans = new AppUserTransaction();
            trans.setOrderNumber(overTransactionDTO.getParentOrderNumber());
            trans.setParentId(BigInteger.ZERO);
            transaction = findOneByEntity(trans);
        }
        Boolean flag = StringUtils.isNotBlank(overTransactionDTO.getCellphone()) && null == appUser || StringUtils.isNotBlank(overTransactionDTO.getParentOrderNumber()) && null == transaction;
        if (flag) {
            return new PageInfo<>();
        }
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "pair_id = ", overTransactionDTO.getPairId());
        ConditionUtil.andCondition(criteria, "order_number = ", overTransactionDTO.getOrderNumber());
        ConditionUtil.andCondition(criteria, "transaction_type = ", overTransactionDTO.getTransactionType());
        ConditionUtil.andCondition(criteria, "self_order = ", 1);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        ConditionUtil.andCondition(criteria, "user_id = ", null == appUser ? null : appUser.getId());
        if (null == transaction) {
            ConditionUtil.andCondition(criteria, "parent_id != ", BigInteger.ZERO);
        } else {
            ConditionUtil.andCondition(criteria, "parent_id = ", transaction.getId());
        }
        ConditionUtil.andCondition(criteria, "status = ", 1);
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        List<AppUserTransaction> list = findByCondition(condition);
        List<OverTransactionVO> vos = new ArrayList<>(list.size());
        PageInfo result = new PageInfo(list);
        for (AppUserTransaction trans : list) {
            OverTransactionVO vo = new OverTransactionVO();
            BeanUtils.copyProperties(trans, vo);
            appUser = appUserService.findById(trans.getUserId());
            CommonPair pair = commonPairService.findById(trans.getPairId());
            AppUserTransaction parent = findById(trans.getParentId());
            vo.setCellphone(appUser.getCellphone());
            vo.setParentOrderNumber(parent.getOrderNumber());
            vo.setPairName(pair.getPairName());
            vo.setBaseTokenName(pair.getBaseTokenName());
            vo.setParentOrderNumber(parent.getOrderNumber());
            vos.add(vo);
        }
        result.setList(vos);
        return result;

    }
}