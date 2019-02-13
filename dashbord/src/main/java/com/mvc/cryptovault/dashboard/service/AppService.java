package com.mvc.cryptovault.dashboard.service;

import com.mvc.cryptovault.common.bean.AppInfo;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qiyichen
 * @create 2019/2/13 15:30
 */
@Service
public class AppService extends BaseService {

    public Boolean saveApp(AppInfo appInfo) {
        Result<Boolean> result = remoteService.saveApp(appInfo);
        return result.getData();
    }

    public AppInfo getApp(String appType) {
        Result<AppInfo> result = remoteService.getApp(appType);
        return result.getData();
    }

    public List<AppInfo> getAppList() {
        Result<List<AppInfo>> result = remoteService.getAppList();
        return result.getData();
    }

}
