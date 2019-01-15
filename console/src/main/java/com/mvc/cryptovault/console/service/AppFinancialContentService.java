package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.AppFinancialContent;
import com.mvc.cryptovault.common.bean.AppUserFinancialPartake;
import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppFinancialContentMapper;
import com.mvc.cryptovault.console.dao.AppUserFinancialPartakeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppFinancialContentService extends AbstractService<AppFinancialContent> implements BaseService<AppFinancialContent> {

    @Autowired
    AppFinancialContentMapper appFinancialContentMapper;
}