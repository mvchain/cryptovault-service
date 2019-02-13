package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppInfo;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppInfoService extends AbstractService<AppInfo> implements BaseService<AppInfo> {

    @Autowired
    private AppInfoMapper appInfoMapper;

}