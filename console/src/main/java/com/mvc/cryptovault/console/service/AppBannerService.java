package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppBanner;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppBannerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppBannerService extends AbstractService<AppBanner> implements BaseService<AppBanner> {

    @Autowired
    AppBannerMapper appBannerMapper;
}