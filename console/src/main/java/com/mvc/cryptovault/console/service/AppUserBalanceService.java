package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppUserBalance;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.dto.AssertVisibleDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.AppUserBalanceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class AppUserBalanceService extends AbstractService<AppUserBalance> implements BaseService<AppUserBalance> {

    @Autowired
    AppUserBalanceMapper appUserBalanceMapper;
    @Autowired
    AppProjectUserTransactionService appProjectUserTransactionService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    AppOrderService appOrderService;

    private Comparator comparator = new Comparator<TokenBalanceVO>() {
        @Override
        public int compare(TokenBalanceVO o1, TokenBalanceVO o2) {
            return o1.getTokenId().compareTo(o2.getTokenId());
        }
    };

    public ProjectBuyVO getBalance(BigInteger userId, AppProject appProject) {
        ProjectBuyVO vo = new ProjectBuyVO();
        vo.setBalance(getBalanceByTokenId(userId, appProject.getBaseTokenId()));
        BigDecimal userBuyTotal = appProjectUserTransactionService.getUserBuyTotal(userId, appProject.getId());
        BigDecimal limit = appProject.getProjectLimit().subtract(userBuyTotal);
        vo.setLimitValue(limit);
        vo.setProjectMin(null == appProject.getProjectMin() ? BigDecimal.ZERO : appProject.getProjectMin());
        return vo;
    }

    public BigDecimal getBalanceByTokenId(BigInteger userId, BigInteger tokenId) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        if (redisTemplate.hasKey(key)) {
            String balance = (String) redisTemplate.boundHashOps(key).get(String.valueOf(tokenId));
            if (StringUtils.isNotBlank(balance)) {
                BigDecimal value = NumberUtils.parseNumber(balance.split("#")[1], BigDecimal.class);
                return value;
            }
        }
        BigDecimal result = null;
        AppUserBalance userBalance = getAppUserBalance(userId, tokenId);
        if (null == userBalance) {
            result = BigDecimal.ZERO;
        } else {
            result = userBalance.getBalance();
        }
        redisTemplate.boundHashOps(key).put(String.valueOf(tokenId), userBalance.getVisible() + "#" + String.valueOf(result));
        return result;
    }

    public void updateBalance(BigInteger userId, BigInteger baseTokenId, BigDecimal value) {
        AppUserBalance userBalance = getAppUserBalance(userId, baseTokenId);
        if (null == userBalance && !baseTokenId.equals(BigInteger.ZERO)) {
            userBalance = new AppUserBalance();
            userBalance.setBalance(BigDecimal.ZERO);
            userBalance.setTokenId(baseTokenId);
            userBalance.setUserId(userId);
            userBalance.setVisible(1);
            save(userBalance);
        }
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        appUserBalanceMapper.updateBalance(userId, baseTokenId, value);
        userBalance = getAppUserBalance(userId, baseTokenId);
        if (null != userBalance) {
            redisTemplate.boundHashOps(key).put(String.valueOf(baseTokenId), userBalance.getVisible() + "#" + String.valueOf(userBalance.getBalance()));
        }
    }

    private AppUserBalance getAppUserBalance(BigInteger userId, BigInteger baseTokenId) {
        AppUserBalance appUserBalance = new AppUserBalance();
        appUserBalance.setTokenId(baseTokenId);
        appUserBalance.setUserId(userId);
        PageHelper.clearPage();
        AppUserBalance balance = appUserBalanceMapper.selectOne(appUserBalance);
        if (null == balance) {
            balance = new AppUserBalance();
            balance.setVisible(0);
            balance.setBalance(BigDecimal.ZERO);
            balance.setTokenId(baseTokenId);
            balance.setUserId(userId);
            save(balance);
            getAsset(userId, true);
        }
        return balance;
    }


    public List<TokenBalanceVO> getAsset(BigInteger userId, Boolean ignoreHide) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        if (!redisTemplate.hasKey(key)) {
            List<AppUserBalance> list = findBy("userId", userId);
            if (null == list) {
                redisTemplate.boundHashOps(key).put("1", "0");
            } else {
                //存储内容为展示状态+余额
                list.stream().forEach(obj -> redisTemplate.boundHashOps(key).put(String.valueOf(obj.getTokenId()), obj.getVisible() + "#" + String.valueOf(obj.getBalance())));
            }
        }
        Map<Object, Object> map = redisTemplate.boundHashOps(key).entries();
        List<TokenBalanceVO> result = new ArrayList<>(map.size());
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            TokenBalanceVO vo = new TokenBalanceVO();
            String value = String.valueOf(entry.getValue());
            Integer visible = NumberUtils.parseNumber(value.split("#")[0], Integer.class);
            BigDecimal balance = NumberUtils.parseNumber(value.split("#")[1], BigDecimal.class);
            vo.setTokenId(NumberUtils.parseNumber(String.valueOf(entry.getKey()), BigInteger.class));
            vo.setValue(balance);
            CommonToken token = commonTokenService.findById(vo.getTokenId());
            if(null == token){
                continue;
            }
            CommonTokenPrice tokenPrice = commonTokenPriceService.findById(vo.getTokenId());
            vo.setRatio(null == tokenPrice ? BigDecimal.ZERO : tokenPrice.getTokenPrice());
            vo.setTokenName(token.getTokenName());
            vo.setTokenImage(token.getTokenImage());
            if (visible == 1 || ignoreHide || token.getId().compareTo(BusinessConstant.BASE_TOKEN_ID_BALANCE) <= 0) {
                result.add(vo);
            }
        }
        initDefault(map, BusinessConstant.BASE_TOKEN_ID_VRT, result);
        initDefault(map, BusinessConstant.BASE_TOKEN_ID_BALANCE, result);
        Collections.sort(result, comparator);
        return result;
    }

    private void initDefault(Map<Object, Object> map, BigInteger tokenId, List<TokenBalanceVO> result) {
        String key = String.valueOf(tokenId);
        if (null == map.get(key)) {
            TokenBalanceVO vo = new TokenBalanceVO();
            CommonToken token = commonTokenService.findById(tokenId);
            CommonTokenPrice tokenPrice = commonTokenPriceService.findById(tokenId);
            vo.setRatio(null == tokenPrice ? BigDecimal.ZERO : tokenPrice.getTokenPrice());
            vo.setTokenName(token.getTokenName());
            vo.setTokenImage(token.getTokenImage());
            vo.setTokenId(tokenId);
            vo.setValue(BigDecimal.ZERO);
            result.add(vo);
        }
    }

    public void debit(BigInteger userId, BigDecimal value, Integer orderType, Integer transferType) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        AppUserBalance balance = getAppUserBalance(userId, BusinessConstant.BASE_TOKEN_ID_BALANCE);
        appUserBalanceMapper.updateBalance(userId, BusinessConstant.BASE_TOKEN_ID_BALANCE, value);
        balance = getAppUserBalance(userId, BusinessConstant.BASE_TOKEN_ID_BALANCE);
        redisTemplate.boundHashOps(key).put(String.valueOf(balance.getTokenId()), balance.getVisible() + "#" + String.valueOf(balance.getBalance()));
        appOrderService.saveHzOrder(value, userId, orderType, transferType);
    }

    public void setAssetVisible(AssertVisibleDTO visibleDTO, BigInteger userId) {
        String regx = "^([0-9]{0,10},)*[0-9]{0,10}$";
        String addStr = visibleDTO.getAddTokenIdArr();
        String removeStr = visibleDTO.getRemoveTokenIdArr();
        if (StringUtils.isNotBlank(addStr)) {
            addStr = addStr.replaceAll(" ", "");
            if (addStr.matches(regx)) {
                appUserBalanceMapper.updateVisiable(userId, addStr, 1);
                insertIfNull(userId, addStr, 1);
            }
        }
        if (StringUtils.isNotBlank(removeStr)) {
            removeStr = removeStr.replaceAll(" ", "");
            if (removeStr.matches(regx)) {
                appUserBalanceMapper.updateVisiable(userId, removeStr, 0);
                insertIfNull(userId, addStr, 0);
            }
        }
        addUserBalance(userId);

    }

    private void insertIfNull(BigInteger userId, String addStr, Integer status) {
        if (StringUtils.isBlank(addStr)) return;
        for (String id : addStr.split(",")) {
            AppUserBalance appUserBalance = new AppUserBalance();
            appUserBalance.setUserId(userId);
            appUserBalance.setTokenId(new BigInteger(id));
            appUserBalance = findOneByEntity(appUserBalance);
            if (null == appUserBalance) {
                appUserBalance = new AppUserBalance();
                appUserBalance.setUserId(userId);
                appUserBalance.setTokenId(new BigInteger(id));
                appUserBalance.setBalance(BigDecimal.ZERO);
                appUserBalance.setVisible(status);
                save(appUserBalance);
            }
        }
    }

    private void addUserBalance(BigInteger userId) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        List<AppUserBalance> list = findBy("userId", userId);
        if (null == list) {
            redisTemplate.boundHashOps(key).put("1", "0");
        } else {
            list.stream().forEach(obj -> redisTemplate.boundHashOps(key).put(String.valueOf(obj.getTokenId()), obj.getVisible() + "#" + String.valueOf(obj.getBalance())));
        }
    }

}