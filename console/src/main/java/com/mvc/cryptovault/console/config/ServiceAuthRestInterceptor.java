package com.mvc.cryptovault.console.config;

import com.mvc.cryptovault.common.util.BaseContextHandler;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qyc
 */
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LocaleContextHolder.setLocale(StringUtils.parseLocale(request.getHeader("Accept-Language")));
        return super.preHandle(request, response, handler);
    }

}
