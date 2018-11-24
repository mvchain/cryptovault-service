package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppOrder;
import com.mvc.cryptovault.common.bean.AppOrderDetail;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockTransactionService extends AbstractService<BlockTransaction> implements BaseService<BlockTransaction> {

    @Autowired
    AppOrderService orderService;
    @Autowired
    AppOrderDetailService appOrderDetailService;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    CommonTokenService tokenService;
    @Autowired
    AppUserService appUserService;

    public void sendTransaction(BigInteger userId, TransactionDTO transactionDTO) {
        Long id = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
        Long now = System.currentTimeMillis();
        BlockTransaction transaction = new BlockTransaction();
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        transaction.setOprType(BusinessConstant.OPR_TYPE_WITHDRAW);
        transaction.setValue(transactionDTO.getValue());
        transaction.setUserId(userId);
        transaction.setTokenId(transactionDTO.getTokenId());
        transaction.setStatus(0);
        transaction.setToAddress(transactionDTO.getAddress());
        transaction.setOrderNumber("P" + String.format("%09d", id));
        save(transaction);
        AppOrder appOrder = new AppOrder();
        appOrder.setClassify(0);
        appOrder.setCreatedAt(now);
        appOrder.setUpdatedAt(now);
        appOrder.setFromAddress("");
        appOrder.setHash("");
        appOrder.setOrderContentId(transaction.getId());
        appOrder.setOrderContentName("BLOCK");
        appOrder.setOrderNumber(transaction.getOrderNumber());
        appOrder.setValue(transactionDTO.getValue());
        appOrder.setUserId(userId);
        appOrder.setTokenId(transactionDTO.getTokenId());
        appOrder.setStatus(0);
        appOrder.setOrderType(1);
        orderService.save(appOrder);
        AppOrderDetail detail = new AppOrderDetail();
        detail.setCreatedAt(now);
        detail.setUpdatedAt(now);
        detail.setFee(BigDecimal.ZERO);
        detail.setFromAddress("");
        detail.setHash("");
        detail.setToAddress(transactionDTO.getAddress());
        detail.setOrderId(appOrder.getId());
        detail.setValue(transactionDTO.getValue());
        appOrderDetailService.save(detail);
        appUserBalanceService.updateBalance(userId, transaction.getTokenId(), BigDecimal.ZERO.subtract(transaction.getValue()));
    }

    public void updateStatus(DBlockStatusDTO dBlockStatusDTO) {
        Condition condition = new Condition(BlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        if (dBlockStatusDTO.getIds().endsWith(",")) {
            dBlockStatusDTO.setIds(dBlockStatusDTO.getIds().substring(0, dBlockStatusDTO.getIds().length() - 1));
        }
        ConditionUtil.andCondition(criteria, String.format("id in (%s)", dBlockStatusDTO.getIds()));
        BlockTransaction transaction = new BlockTransaction();
        Integer transactionStatus = dBlockStatusDTO.getStatus() == 1 ? 2 : 3;
        transaction.setTransactionStatus(transactionStatus);
        mapper.updateByConditionSelective(transaction, condition);
    }

    public PageInfo<DBlockeTransactionVO> getTransactions(PageDTO pageDTO, DBlockeTransactionDTO dBlockeTransactionDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        Condition condition = new Condition(BlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "transaction_status = ", dBlockeTransactionDTO.getTransactionStatus());
        ConditionUtil.andCondition(criteria, "to_address = ", dBlockeTransactionDTO.getToAddress());
        ConditionUtil.andCondition(criteria, "order_number = ", dBlockeTransactionDTO.getOrderNumber());
        ConditionUtil.andCondition(criteria, "opr_type = ", dBlockeTransactionDTO.getOprType());
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<BlockTransaction> list = findByCondition(condition);
        List<DBlockeTransactionVO> vos = new ArrayList<>(list.size());
        PageInfo result = new PageInfo(list);
        for (BlockTransaction blockTransaction : list) {
            DBlockeTransactionVO vo = new DBlockeTransactionVO();
            BeanUtils.copyProperties(blockTransaction, vo);
            vo.setTokenName(tokenService.findById(vo.getTokenId()).getTokenName());
            vo.setCellphone(appUserService.findById(blockTransaction.getUserId()).getCellphone());
            vos.add(vo);
        }
        result.setList(vos);
        return result;
    }
}