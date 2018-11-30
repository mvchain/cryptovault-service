package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.ExportOrders;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.CommonAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class CommonAddressService extends AbstractService<CommonAddress> implements BaseService<CommonAddress> {

    @Autowired
    CommonAddressMapper commonAddressMapper;
    @Autowired
    CommonTokenService commonTokenService;

    public List<ExportOrders> exportCollect() {
        Condition condition = new Condition(CommonAddress.class);
        var criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "used = ", 1);
        ConditionUtil.andCondition(criteria, "userId != ", BigInteger.ZERO);
        ConditionUtil.andCondition(criteria, "balance > ", BigDecimal.ZERO);
        ConditionUtil.andCondition(criteria, "used", 1);
        return null;
    }

}