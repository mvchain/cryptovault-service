package com.mvc.cryptovault.consumer.service;

import com.mvc.cryptovault.common.bean.AppOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author qiyichen
 * @create 2018/12/27 15:01
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppOrderService extends BaseService<AppOrder> {



}
