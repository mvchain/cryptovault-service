package com.mvc.cryptovault.dashboard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BlockService extends BaseService {


}
