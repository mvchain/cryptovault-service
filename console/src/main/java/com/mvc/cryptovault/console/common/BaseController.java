package com.mvc.cryptovault.console.common;

import com.mvc.cryptovault.console.service.*;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author qiyichen
 * @create 2018/11/12 14:36
 */
public class BaseController {
    @Autowired
    protected
    AdminPermissionService adminPermissionService;
    @Autowired
    protected
    AdminUserPermissionService adminUserPermissionService;
    @Autowired
    protected
    AdminUserService adminUserService;
    @Autowired
    protected
    AppKlineService appKlineService;
    @Autowired
    protected
    AppMessageService appMessageService;
    @Autowired
    protected
    AppOrderDetailService appOrderDetailService;
    @Autowired
    protected
    AppOrderService appOrderService;
    @Autowired
    protected
    AppProjectService appProjectService;
    @Autowired
    protected
    AppProjectUserTransactionService appProjectUserTransactionService;
    @Autowired
    protected
    AppUserAddressService appUserAddressService;
    @Autowired
    protected
    CommonAddressService commonAddressService;
    @Autowired
    protected
    AppUserBalanceService appUserBalanceService;
    @Autowired
    protected
    AppUserOprLogService appUserOprLogService;
    @Autowired
    protected
    AppUserService appUserService;
    @Autowired
    protected
    AppUserTransactionService appUserTransactionService;
    @Autowired
    protected
    BlockHeightService blockHeightService;
    @Autowired
    protected
    BlockTransactionService blockTransactionService;
    @Autowired
    protected
    CommonPairService commonPairService;
    @Autowired
    protected
    CommonTokenControlNextService commonTokenControlNextService;
    @Autowired
    protected
    CommonTokenControlService commonTokenControlService;
    @Autowired
    protected
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    protected
    CommonTokenService commonTokenService;

    @Autowired
    protected
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    protected HTreeMap hTreeMap;
}
