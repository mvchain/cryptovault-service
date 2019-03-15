package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.ExplorerBlockInfo;
import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.common.bean.ExplorerBlockTransaction;
import com.mvc.cryptovault.common.bean.ExplorerBlockUser;
import com.mvc.cryptovault.common.bean.vo.ExplorerSimpleVO;
import com.mvc.cryptovault.common.bean.vo.ExplorerTransactionDetailVO;
import com.mvc.cryptovault.common.bean.vo.ExplorerTransactionSimpleVO;
import com.mvc.cryptovault.common.bean.vo.NowBlockVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.ExplorerBlockInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2019/3/12 13:17
 */
@Service
public class ExplorerBlockInfoService extends AbstractService<ExplorerBlockInfo> implements BaseService<ExplorerBlockInfo> {

    @Autowired
    ExplorerBlockInfoMapper explorerBlockInfoMapper;
    @Autowired
    ExplorerBlockSettingService explorerBlockSettingService;
    @Autowired
    ExplorerBlockTransactionService blockTransactionService;
    @Autowired
    ExplorerBlockUserService explorerBlockUserService;
    @Autowired
    ExplorerBlockSettingService settingService;

    public volatile ExplorerBlockInfo nowHeight = new ExplorerBlockInfo();

    @Transactional(propagation = Propagation.NOT_SUPPORTED, rollbackFor = Exception.class)
    public void saveInfo(ExplorerBlockInfo explorerBlockInfo) {
        int num = explorerBlockInfoMapper.insert(explorerBlockInfo);
        if (num == 1) {
            nowHeight = explorerBlockInfo;
            settingService.updateValue(explorerBlockInfo.getTransactions());
            redisTemplate.opsForValue().set("EXPLORER_NOW", JSON.toJSONString(nowHeight));
        }
    }

    public ExplorerBlockInfo getLast() {
        PageHelper.startPage(1, 1, "id desc");
        List<ExplorerBlockInfo> result = explorerBlockInfoMapper.selectAll();
        if (null == result || result.size() == 0) {
            return null;
        }
        ExplorerBlockInfo info = result.get(0);
        redisTemplate.opsForValue().set("EXPLORER_NOW", JSON.toJSONString(info));
        return info;
    }

    public NowBlockVO getLastVO() {
        ExplorerBlockInfo info = getNowHeight();
        ExplorerBlockSetting block = explorerBlockSettingService.findById(BigInteger.ONE);
        NowBlockVO vo = new NowBlockVO();
        vo.setBlockId(info.getId());
        vo.setConfirmTime(info.getCreatedAt());
        vo.setServiceTime(System.currentTimeMillis());
        vo.setTotal(block.getTotal());
        vo.setTransactionCount(block.getTotalTransaction());
        vo.setVersion(block.getVersion());
        return vo;
    }

    public List<ExplorerTransactionSimpleVO> getLastTransaction(Integer pageSize) {
        return getBlockTx(getNowHeight().getId(), null, pageSize);
    }

    public List<ExplorerSimpleVO> getBlocks(BigInteger blockId, Integer pageSize) {
        PageHelper.startPage(1, pageSize, "id desc");
        Condition condition = new Condition(ExplorerBlockInfo.class);
        Example.Criteria criteria = condition.createCriteria();
        if (null != blockId && !blockId.equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", blockId);
        }
        List<ExplorerBlockInfo> list = findByCondition(condition);
        return list.stream().map(obj -> {
            ExplorerSimpleVO vo = new ExplorerSimpleVO();
            vo.setTransactions(obj.getTransactions());
            vo.setCreatedAt(obj.getCreatedAt());
            vo.setBlockId(obj.getId());
            return vo;
        }).collect(Collectors.toList());
    }

    public ExplorerSimpleVO getBlockInfo(BigInteger blockId) {
        ExplorerBlockInfo block = findById(blockId);
        if (null == block) {
            return new ExplorerSimpleVO();
        }
        ExplorerSimpleVO result = new ExplorerSimpleVO();
        result.setBlockId(block.getId());
        result.setCreatedAt(block.getCreatedAt());
        result.setTransactions(block.getTransactions());
        return result;
    }

    private ExplorerBlockInfo getNowHeight(){
        String obj = redisTemplate.opsForValue().get("EXPLORER_NOW");
        if(null != obj){
            return JSON.parseObject(obj, ExplorerBlockInfo.class);
        }
        nowHeight = getLast();
        return nowHeight;
    }

    public List<ExplorerTransactionSimpleVO> getBlockTx(BigInteger blockId, BigInteger transactionId, Integer pageSize) {
        BigInteger now = getNowHeight().getId();
        if (null == blockId || blockId.equals(BigInteger.ZERO)) {
            blockId = BigInteger.ONE;
        }
        ExplorerBlockInfo block = findById(blockId);
        Integer start = ExplorerBlockTransaction.getIndex(blockId);
        Condition condition = new Condition(ExplorerBlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.startPage(1, pageSize, "id asc");
        ConditionUtil.andCondition(criteria, "id >= ", BigInteger.valueOf(start));
        ConditionUtil.andCondition(criteria, "id < ", BigInteger.valueOf(start + block.getTransactions()));
        if (null != transactionId && !BigInteger.ZERO.equals(transactionId)) {
            ConditionUtil.andCondition(criteria, "id >", transactionId);
        }
        List<ExplorerBlockTransaction> list = blockTransactionService.findByCondition(condition);
        List<ExplorerTransactionSimpleVO> result = list.stream().map(obj -> {
            ExplorerTransactionSimpleVO vo = new ExplorerTransactionSimpleVO();
            vo.setConfirm(now.subtract(block.getId()));
            vo.setCreatedAt(block.getCreatedAt());
            vo.setHash(ExplorerBlockTransaction.combineHash(now, obj.getHash()));
            vo.setHeight(block.getId());
            vo.setTransactionId(obj.getId());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    public ExplorerTransactionDetailVO getDetail(String hash) {
        try {
            BigInteger blockId = ExplorerBlockTransaction.getBlock(hash);
            String realHash = ExplorerBlockTransaction.getRealHash(hash);
            ExplorerBlockTransaction tx = blockTransactionService.findOneBy("hash", realHash);
            ExplorerBlockUser from = explorerBlockUserService.findById(tx.getFromUserId());
            ExplorerBlockUser to = explorerBlockUserService.findById(tx.getToUserId());
            ExplorerBlockInfo block = findById(blockId);
            ExplorerTransactionDetailVO result = new ExplorerTransactionDetailVO();
            result.setConfirm(getNowHeight().getId().subtract(blockId).intValue());
            result.setCreatedAt(block.getCreatedAt());
            result.setFrom(from.getPublicKey());
            result.setHash(realHash);
            result.setHeight(blockId);
            result.setTo(to.getPublicKey());
            result.setTokenName(tx.getTokenName());
            result.setValue(tx.getValue());
            return result;
        } catch (Exception e) {
            return new ExplorerTransactionDetailVO();
        }
    }

}
