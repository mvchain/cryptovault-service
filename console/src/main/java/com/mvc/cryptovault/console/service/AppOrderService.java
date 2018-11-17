package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppOrder;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.dto.TransactionSearchDTO;
import com.mvc.cryptovault.common.bean.vo.TransactionDetailVO;
import com.mvc.cryptovault.common.bean.vo.TransactionSimpleVO;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppOrderService extends AbstractService<AppOrder> implements BaseService<AppOrder> {

    @Autowired
    AppOrderDetailService appOrderDetailService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;

    public TransactionDetailVO getDetail(BigInteger userId, BigInteger id) {
        var order = findById(id);
        Assert.isTrue(userId.equals(order.getUserId()), MessageConstants.getMsg("PERMISSION_WRONG"));
        var detail = appOrderDetailService.findById(id);
        var token = commonTokenService.findById(order.getTokenId());
        TransactionDetailVO vo = new TransactionDetailVO();
        vo.setClassify(order.getClassify());
        vo.setCreatedAt(order.getCreatedAt());
        vo.setFee(detail.getFee());
        vo.setFeeTokenType(token.getTokenType());
        vo.setHashLink(token.getLink() + order.getHash());
        vo.setTokenName(token.getTokenName());
        vo.setFromAddress(order.getFromAddress());
        vo.setHash(order.getHash());
        vo.setStatus(order.getStatus());
        vo.setToAddress(detail.getToAddress());
        vo.setUpdatedAt(order.getUpdatedAt());
        vo.setValue(order.getValue());
        return vo;
    }

    public List<TransactionSimpleVO> getTransactions(BigInteger userId, TransactionSearchDTO transactionSearchDTO) {
        //TODO 暂时从数据库查询,后续优化
        Condition condition = new Condition(AppOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        ConditionUtil.andCondition(criteria, "order_type = ", transactionSearchDTO.getTransactionType());
        PageHelper.startPage(1, transactionSearchDTO.getPageSize());
        PageHelper.orderBy("id desc");
        if (BusinessConstant.SEARCH_DIRECTION_UP.equals(transactionSearchDTO.getType()) && (null != transactionSearchDTO.getId()) && !transactionSearchDTO.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id > ", transactionSearchDTO.getId());
        } else if (BusinessConstant.SEARCH_DIRECTION_DOWN.equals(transactionSearchDTO.getType()) && (null != transactionSearchDTO.getId()) && !transactionSearchDTO.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id <", transactionSearchDTO.getId());
        }
        List<AppOrder> list = findByCondition(condition);
        List<TransactionSimpleVO> result = new ArrayList<>(list.size());
        list.forEach(obj -> {
            CommonToken token = commonTokenService.findById(obj.getTokenId());
            CommonTokenPrice price = commonTokenPriceService.findById(token.getId());
            TransactionSimpleVO vo = new TransactionSimpleVO();
            BeanUtils.copyProperties(obj, vo);
            vo.setTokenName(token.getTokenName());
            vo.setRatio(price.getTokenPrice());
            vo.setTransactionType(obj.getOrderType());
            result.add(vo);
        });
        return result;
    }
}