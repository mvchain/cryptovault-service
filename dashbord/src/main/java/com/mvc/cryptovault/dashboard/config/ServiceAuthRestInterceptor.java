package com.mvc.cryptovault.dashboard.config;

import com.mvc.cryptovault.common.dashboard.bean.vo.AdminDetailVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.permission.PermissionCheck;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.JwtHelper;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.common.util.TokenErrorException;
import com.mvc.cryptovault.dashboard.service.AdminService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author qyc
 */
@Component
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    AdminService adminService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = getAbsUri(request.getRequestURI());
        if (uri.startsWith("null") || uri.startsWith("swagger-ui.html")) {
            uri = uri.replaceFirst("null", "/").replaceFirst("swagger-ui.html", "/");
            response.sendRedirect("/" + getAbsUri(uri));
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = request.getHeader("Authorization");
        Claims claim = JwtHelper.parseJWT(token);
        NotLogin loginAnn = handlerMethod.getMethodAnnotation(NotLogin.class);
        PermissionCheck permissionCheck = handlerMethod.getMethodAnnotation(PermissionCheck.class);
        checkAnnotation(claim, loginAnn, request.getRequestURI());
        checkPermission(claim, permissionCheck);
        setUserInfo(claim);
        return super.preHandle(request, response, handler);
    }

    private String getAbsUri(String requestURI) {
        while (requestURI.startsWith("/")) {
            requestURI = requestURI.replaceFirst("/", "");
        }
        return requestURI;
    }

    private void checkPermission(Claims claim, PermissionCheck permissionCheck) {
        if (null == claim) {
            return;
        }
        Integer userId = claim.get("userId", Integer.class);
        if (BigInteger.ZERO.equals(userId) || null == permissionCheck) {
            return;
        }
        String permissionStr = redisTemplate.opsForValue().get("ADMIN_PERMISSON_" + userId);
        if (StringUtils.isBlank(permissionStr)) {
            AdminDetailVO user = adminService.getAdminDetail(BigInteger.valueOf(userId));
            permissionStr = user.getPermissions();
            if (StringUtils.isBlank(permissionStr)) {
                throw new IllegalArgumentException("没有权限,请联系管理员");
            }
            redisTemplate.opsForValue().set("ADMIN_PERMISSON_" + userId, permissionStr);
        }
        String needPermission = permissionCheck.value();
        String permissionArr[] = permissionStr.split(",");
        if (!Arrays.asList(permissionArr).contains(needPermission)) {
            throw new IllegalArgumentException("没有权限,请联系管理员");
        }
    }

    //校验权限
    private void checkAnnotation(Claims claim, NotLogin loginAnn, String uri) throws LoginException {
        if (null == claim && null == loginAnn) {
            if (uri.indexOf("/refresh") > 0) {
                throw new LoginException(MessageConstants.getMsg("TOKEN_EXPIRE"));
            } else {
                throw new TokenErrorException(MessageConstants.getMsg("TOKEN_EXPIRE"), MessageConstants.TOKEN_EXPIRE_CODE);
            }
        }
        if (null != claim) {
            JwtHelper.check(claim, uri);
        }
    }

    //设置用户信息
    public void setUserInfo(Claims userInfo) {
        if (null != userInfo) {
            String username = userInfo.get("username", String.class);
            Integer userId = userInfo.get("userId", Integer.class);
            BaseContextHandler.set("username", username);
            BaseContextHandler.set("userId", BigInteger.valueOf(userId.longValue()));
        }
    }

}
