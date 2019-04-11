package com.mvc.cryptovault.dashboard.service;

import com.mvc.cryptovault.common.bean.AppBanner;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BannerService extends BaseService {

    public List<AppBanner> bannerList() {
        Result<List<AppBanner>> result = remoteService.bannerList();
        return result.getData();
    }

    public Boolean deleteBanner(BigInteger id) {
        Result<Boolean> result = remoteService.delBanner(id);
        return result.getData();
    }

    public Boolean saveBanner(AppBanner appBanner) {
        Result<Boolean> result = remoteService.saveBanner(appBanner);
        return result.getData();
    }

}
