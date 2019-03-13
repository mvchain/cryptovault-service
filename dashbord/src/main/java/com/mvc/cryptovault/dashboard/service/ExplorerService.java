package com.mvc.cryptovault.dashboard.service;

import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.stereotype.Service;

/**
 * @author qiyichen
 * @create 2019/2/13 15:30
 */
@Service
public class ExplorerService extends BaseService {

    public ExplorerBlockSetting getExplorerSetting() {
        Result<ExplorerBlockSetting> result = remoteService.getExplorerSettting();
        return result.getData();
    }

    public Boolean save(ExplorerBlockSetting setting) {
        Result<Boolean> result = remoteService.saveExplorerSetting(setting);
        return result.getData();
    }
}
