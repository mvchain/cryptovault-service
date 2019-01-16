package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppFinancialDetail;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppFinancialDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppFinancialDetailService extends AbstractService<AppFinancialDetail> implements BaseService<AppFinancialDetail> {

    @Autowired
    private AppFinancialDetailMapper appFinancialDetailMapper;

    public List<AppFinancialDetail> findDetails(BigInteger id) {
        PageHelper.orderBy("depth desc");
        return findBy("financialId", id);
    }

    public void updateDetail(BigInteger id, List<AppFinancialDetail> details) {
        AppFinancialDetail detail = new AppFinancialDetail();
        detail.setFinancialId(id);
        appFinancialDetailMapper.delete(detail);
        details.forEach(obj->{obj.setFinancialId(id);save(obj);});
    }
}