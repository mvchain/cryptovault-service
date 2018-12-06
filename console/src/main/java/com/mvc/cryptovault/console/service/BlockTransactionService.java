package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppOrder;
import com.mvc.cryptovault.common.bean.AppOrderDetail;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.BlockTransactionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
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
    @Autowired
    BlockTransactionMapper blockTransactionMapper;
    @Autowired
    CommonAddressService commonAddressService;

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
        transaction.setTokenType(transactionDTO.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_USDT) ? "BTC" : "ETH");
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
        ConditionUtil.andCondition(criteria, "to_address = ", dBlockeTransactionDTO.getFromAddress());
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

    /**
     * 更新成功记录
     *
     * @param obj
     */
    public void updateSuccess(BlockTransaction obj) {
        int num = blockTransactionMapper.updateSuccess(obj, System.currentTimeMillis());
        if (num == 1 && !obj.getUserId().equals(BigInteger.ZERO) && obj.getOprType() == 1) {
            //只有在更新成功的情况下修改余额,更新冲突时忽略,提现在申请时就已经扣款,因此只有充值需要更新余额
            appUserBalanceService.updateBalance(obj.getUserId(), obj.getTokenId(), obj.getValue());
        }
    }

    public List<BlockTransaction> getSign() {
        Condition condition = new Condition(BlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "transaction_status in (2, 6)");
        ConditionUtil.andCondition(criteria, "opr_type = ", 2);
        List<BlockTransaction> list = findByCondition(condition);
        return list;
    }

    public void updateHash(String orderId, String hash) {
        if (StringUtils.isNotBlank(orderId)) {
            blockTransactionMapper.updateHash(orderId, hash);
        }
    }

    public Boolean saveTrans(BlockTransaction blockTransaction) {
        save(blockTransaction);
        if (findBy("hash", blockTransaction.getHash()).size() > 1) {
            //插入后如果数据重复则删除
            deleteById(blockTransaction.getId());
            return null;
        }
        if (!blockTransaction.getUserId().equals(BigInteger.ZERO)) {
            orderService.saveOrder(blockTransaction);
            orderService.saveOrderTarget(blockTransaction);
        }
        return true;
    }

    public void updateTrans(BlockTransaction oldTrans, BlockTransaction blockTransaction) {
        //记录当前是否为第一次由非成功变为成功的状态
        Boolean flag = oldTrans.getStatus() != 2 && blockTransaction.getStatus() == 2;
        oldTrans.setTransactionStatus(blockTransaction.getTransactionStatus());
        //已经成功的记录不修改,防止余额重复累加
        if (oldTrans.getStatus() != 2) {
            oldTrans.setStatus(blockTransaction.getStatus());
        }
        oldTrans.setUpdatedAt(System.currentTimeMillis());
        oldTrans.setHeight(blockTransaction.getHeight());
        oldTrans.setFee(blockTransaction.getFee());
        oldTrans.setErrorData(blockTransaction.getErrorData());
        oldTrans.setFromAddress(blockTransaction.getFromAddress());
        oldTrans.setErrorMsg(blockTransaction.getErrorMsg());
        update(oldTrans);
        if (!blockTransaction.getUserId().equals(BigInteger.ZERO) && flag) {
            List<AppOrder> orders = orderService.findBy("hash", oldTrans.getHash());
            orders.forEach(obj -> {
                orderService.updateOrder(obj);
            });
        }
    }

}