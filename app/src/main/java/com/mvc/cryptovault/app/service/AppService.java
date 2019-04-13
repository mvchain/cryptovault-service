package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.AppBanner;
import com.mvc.cryptovault.common.bean.AppInfo;
import com.mvc.cryptovault.common.bean.dto.AssertVisibleDTO;
import com.mvc.cryptovault.common.bean.dto.DebitDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionSearchDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.util.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AppService {

    @Autowired
    ConsoleRemoteService consoleRemoteService;

    public AppInfo getApp(String appType) {
        Result<AppInfo> appInfoResult = consoleRemoteService.getApp(appType);
        return appInfoResult.getData();
    }

    public String getView(Integer type, BigInteger id) {
        Result<String> result = consoleRemoteService.getView(type, id);
        return result.getData();
    }

    public List<AppBanner> bannerList() {
        Result<List<AppBanner>> result = consoleRemoteService.bannerList();
        return result.getData();
    }
}
