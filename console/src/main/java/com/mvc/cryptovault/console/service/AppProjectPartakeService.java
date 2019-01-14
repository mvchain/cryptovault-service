package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppOrder;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppProjectPartake;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppProjectPartakeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppProjectPartakeService extends AbstractService<AppProjectPartake> implements BaseService<AppProjectPartake> {

    @Autowired
    AppProjectPartakeMapper appProjectPartakeMapper;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Value("${project.frequency}")
    Long frequency;
    @Autowired
    AppMessageService appMessageService;
    @Autowired
    AppProjectService appProjectService;
    @Autowired
    AppOrderService appOrderService;

    public void savePartake(ImportPartake partake, AppProject appProject) {
        AppProjectPartake appProjectPartake = new AppProjectPartake();
        appProjectPartake.setProjectId(partake.getProjectId());
        appProjectPartake.setUserId(partake.getUserId());
        appProjectPartake = findOneByEntity(appProjectPartake);
        if (appProjectPartake == null) {
            appProjectPartake = new AppProjectPartake();
            appProjectPartake.setProjectId(partake.getProjectId());
            appProjectPartake.setUserId(partake.getUserId());
            appProjectPartake.setTokenId(appProject.getTokenId());
            appProjectPartake.setValue(partake.getValue());
            appProjectPartake.setPublishTime(appProject.getPublishAt());
            appProjectPartake.setTimes(new Float(100f / appProject.getReleaseValue()).intValue());
            appProjectPartake.setReverseValue(appProjectPartake.getValue().divide(BigDecimal.valueOf(appProjectPartake.getTimes())));
            save(appProjectPartake);
        }
    }

    public void sendProject() {
        Long time = System.currentTimeMillis();
        Condition condition = new Condition(AppProjectPartake.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "publish_time <= ", time);
        ConditionUtil.andCondition(criteria, "times > ", 0);
        List<AppProjectPartake> list = appProjectPartakeMapper.selectByCondition(condition);
        Map<BigInteger, AppProject> projectMap = new HashMap<>(5);
        List<AppOrder> orders = new ArrayList<>(list.size());
        list.forEach(appProjectPartake -> {
            //统计所有项目,统一发送推送
            AppProject appProject = projectMap.get(appProjectPartake.getProjectId());
            if(null ==appProject ){
                appProject =  appProjectService.findById(appProjectPartake.getProjectId());
                projectMap.put(appProjectPartake.getProjectId(),appProject);
            }
            appProjectPartake.setTimes(appProjectPartake.getTimes() - 1);
            //没有释放完毕,更新下一次推送时间为下一个周期
            if (appProjectPartake.getTimes() > 0) {
                appProjectPartake.setPublishTime(appProjectPartake.getPublishTime() + frequency);
            }
            update(appProjectPartake);
            //添加到统一订单列表
            AppOrder order = appOrderService.saveOrder(appProjectPartake, appProject);
            orders.add(order);
            //更新余额
            appUserBalanceService.updateBalance(appProjectPartake.getUserId(), appProjectPartake.getTokenId(), appProjectPartake.getReverseValue());
        });
        //TODO 意外停止服务后需要将message表中不存在的orderid对应数据重新发送
        for (BigInteger key : projectMap.keySet()) {
            AppProject project = projectMap.get(key);
            if (null != project) {
                appMessageService.sendPublish(project.getId(), project.getProjectName(), time, project.getTokenName(), orders);
            }
        }
    }

    public String getTag(BigInteger userId) {
        String result = appProjectPartakeMapper.getTag(userId);
        return result;
    }
}