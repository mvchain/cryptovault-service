package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.ExplorerBlockSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 13:17
 */
@Service
public class ExplorerBlockSettingService extends AbstractService<ExplorerBlockSetting> implements BaseService<ExplorerBlockSetting> {

    @Autowired
    ExplorerBlockSettingMapper explorerBlockSettingMapper;

    public void updateValue(Integer transactions) {
        explorerBlockSettingMapper.updateValue(transactions);
        updateCache(BigInteger.ONE);
    }
}
