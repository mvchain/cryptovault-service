package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AdminUser;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.AdminPasswordDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUserDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.AdminVO;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.JwtHelper;
import com.mvc.cryptovault.common.util.MessageConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdminService extends BaseService {

    public PageInfo<AdminVO> getAdmins(PageDTO dto) {
        Result<PageInfo<AdminVO>> result = remoteService.getAdmins(getUserId(), dto);
        return result.getData();
    }

    public AdminDetailVO getAdminDetail(BigInteger id) {
        Result<AdminDetailVO> result = remoteService.getAdminDetail(id);
        return result.getData();
    }

    public Boolean deleteAdmin(BigInteger id) {
        Result<Boolean> result = remoteService.deleteAdmin(getUserId(), id);
        return result.getData();
    }

    public Boolean updatePwd(BigInteger id, AdminPasswordDTO adminPasswordDTO) {
        Result<Boolean> result = remoteService.updatePwd(getUserId(), adminPasswordDTO);
        return result.getData();
    }

    public TokenVO login(DUserDTO userDTO) {
        TokenVO vo = new TokenVO();
        Result<AdminUser> userResult = remoteService.getAdminByUsername(userDTO.getUsername());
        AdminUser user = userResult.getData();
        Assert.notNull(null != user, MessageConstants.getMsg("USER_NOT_EXIST"));
        Boolean passwordCheck = user.getPassword().equals(userDTO.getPassword());
        Assert.isTrue(passwordCheck, MessageConstants.getMsg("USER_PASS_WRONG"));
        Assert.isTrue(user.getStatus() == 1, "用户已冻结");
        String token = JwtHelper.createToken(userDTO.getUsername(), user.getId());
        String refreshToken = JwtHelper.createRefresh(userDTO.getUsername(), user.getId());
        //密码正确后清空错误次数
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        vo.setUserId(user.getId());
        return vo;
    }

    public String refresh() {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        String username = (String) BaseContextHandler.get("username");
        return JwtHelper.createToken(username, userId);
    }

    public String getSign() {
        BigInteger userId = null;
        try {
            userId = (BigInteger) BaseContextHandler.get("userId");
        } catch (Exception e) {
            userId = BigInteger.valueOf((Integer) BaseContextHandler.get("userId"));
        }
        String key = RedisConstant.EXPORT + userId;
        String sign = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(key, sign + userId);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        return sign + userId;
    }

    public Boolean newAdmin(AdminDTO adminDTO) {
        Result<Boolean> result = remoteService.newAdmin(adminDTO);
        return result.getData();
    }

    public Boolean updateAdmin(AdminDTO adminDTO) {
        Result<Boolean> result = remoteService.updateAdmin(getUserId(), adminDTO);
        return result.getData();
    }
}
