package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppMessage;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.dto.TimeSearchDTO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.List;

@Service
public class AppMessageService extends AbstractService<AppMessage> implements BaseService<AppMessage> {

    public List<AppMessage> list(BigInteger userId, TimeSearchDTO timeSearchDTO, PageDTO pageDTO) {
        Condition condition = new Condition(AppMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        PageHelper.startPage(1, pageDTO.getPageSize());
        PageHelper.orderBy("created_at desc");
        if (timeSearchDTO.getType() == 0 && null != timeSearchDTO.getTimestamp()) {
            ConditionUtil.andCondition(criteria, "created_at > ", timeSearchDTO.getTimestamp());
        } else if (timeSearchDTO.getType() == 1 && null != timeSearchDTO.getTimestamp()) {
            ConditionUtil.andCondition(criteria, "created_at < ", timeSearchDTO.getTimestamp());
        }
        return findByCondition(condition);
    }
}