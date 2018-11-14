package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.MyOrderVO;
import com.mvc.cryptovault.common.bean.vo.OrderVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

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

    public List<OrderVO> getTransactions(OrderDTO dto) {
        Condition condition = new Condition(AppUserTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "parent_id", 0);
        ConditionUtil.andCondition(criteria, "status", 0);
        ConditionUtil.andCondition(criteria, "transaction_type", dto.getTransactionType());
        ConditionUtil.andCondition(criteria, "pair_id", dto.getPairId());
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
        ConditionUtil.andCondition(criteria, "pair_id", dto.getPairId());
        ConditionUtil.andCondition(criteria, "status", dto.getStatus());
        ConditionUtil.andCondition(criteria, "transaction_type", dto.getTransactionType());
        ConditionUtil.andCondition(criteria, "user_id", userId);
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
            vo.setNickname(user.getNickname());
            result.add(vo);
        });
        return result;
    }

    //TODO 步骤较多,异步化
    public void buy(BigInteger userId, TransactionBuyDTO dto) {
        //校验余额是否足够
        checkBalance(userId, dto);
        //校验浮动范围是否正确
        checkPrice(dto);
        //校验可购买量是否足够
        checkValue(dto);
        if (null == dto.getId()) {
            //普通挂单
        } else {
            //修改主单购买信息

            //生成用户主动交易记录

            //生成目标用户交易记录

        }
    }

    public void cancel(BigInteger userId, BigInteger id) {
        AppUserTransaction trans = findById(id);
        trans.setStatus(BusinessConstant.STATUS_CANCEL);
        trans.setUpdatedAt(System.currentTimeMillis());
        update(trans);
    }

}