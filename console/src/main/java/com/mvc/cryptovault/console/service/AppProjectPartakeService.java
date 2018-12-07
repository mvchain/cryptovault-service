package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppProjectPartake;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AppProjectPartakeService extends AbstractService<AppProjectPartake> implements BaseService<AppProjectPartake> {

    public void savePartake(ImportPartake partake, AppProject appProject) {
        AppProjectPartake appProjectPartake = new AppProjectPartake();
        appProjectPartake.setProjectId(partake.getProjectId());
        appProjectPartake.setUserId(partake.getUserId());
        appProjectPartake = findOneByEntity(appProjectPartake);
        if (appProjectPartake == null) {
            appProjectPartake = new AppProjectPartake();
            appProjectPartake.setProjectId(partake.getProjectId());
            appProjectPartake.setUserId(partake.getUserId());
            appProjectPartake.setValue(partake.getValue());
            appProjectPartake.setTimes(new Float(100f / appProject.getReleaseValue()).intValue());
            appProjectPartake.setReverseValue(appProjectPartake.getValue().divide(BigDecimal.valueOf(appProjectPartake.getTimes())));
            save(appProjectPartake);
        }
    }
}