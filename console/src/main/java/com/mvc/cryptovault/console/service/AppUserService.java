package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppMessage;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
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
public class AppUserService extends AbstractService<AppUser> implements BaseService<AppUser> {

    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppMessageService appMessageService;
    @Autowired
    AppProjectPartakeService appProjectPartakeService;

    public PageInfo<DUSerVO> findUser(PageDTO pageDTO, String cellphone, Integer status) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), "id desc");
        Condition condition = new Condition(AppUser.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "cellphone = ", cellphone);
        ConditionUtil.andCondition(criteria, "status = ", status);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<AppUser> list = findByCondition(condition);
        List<DUSerVO> vos = new ArrayList<>(list.size());
        for (AppUser appUser : list) {
            DUSerVO vo = new DUSerVO();
            BeanUtils.copyProperties(appUser, vo);
            List<TokenBalanceVO> data = appUserBalanceService.getAsset(appUser.getId(), true);
            BigDecimal sum = data.stream().map(obj -> obj.getRatio().multiply(obj.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setBalance(sum);
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public PageInfo<DUserLogVO> getUserLog(BigInteger id, PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        Condition condition = new Condition(AppMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", id);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<AppMessage> list = appMessageService.findByCondition(condition);
        List<DUserLogVO> vos = new ArrayList<>(list.size());
        for (AppMessage appUser : list) {
            DUserLogVO vo = new DUserLogVO();
            BeanUtils.copyProperties(appUser, vo);
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public String getTag(BigInteger userId) {
        String tag = appProjectPartakeService.getTag(userId);
        return tag;
    }

}