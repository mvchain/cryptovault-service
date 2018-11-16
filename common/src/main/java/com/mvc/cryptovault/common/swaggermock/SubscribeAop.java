package com.mvc.cryptovault.common.swaggermock;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
//@Component
@ConfigurationProperties(
        prefix = "swaggermock",
        ignoreUnknownFields = true
)
public class SubscribeAop {

    @Autowired
    Environment environment;

    /**
     * 用于记录请求调用失败时的时间戳
     */
    private final Map<String, Long> map = new ConcurrentHashMap<>();
    private Long retry = null;
    private Boolean switchOn = null;

    /**
     * 对mock注解进行拦截
     */
    @Pointcut("@annotation(com.mvc.cryptovault.common.swaggermock.SwaggerMock)")
    private void annotation() {
    }

    private String formatParamCode(String paramCode) {
        return paramCode.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "");
    }

    private Result getResult(String methodName, Class<?> classTarget, ProceedingJoinPoint pjp) throws Throwable {
        Result result = null;
        Class<?>[] par = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        Method objMethod = classTarget.getMethod(methodName, par);
        String key = classTarget.getSimpleName() + "." + methodName;
        SwaggerMock ann = objMethod.getAnnotation(SwaggerMock.class);
        String jsonStr = ann.value();
        if (jsonStr.matches("(\\$\\{[^\\}]+})")) {
            jsonStr = environment.getProperty(formatParamCode(jsonStr));
        }
        result = JSON.parseObject(jsonStr, Result.class);
        Long time = map.get(key);
        String str = String.format("调用失败,当前为Mock数据,%s 毫秒后重新尝试, 上一次调用时间为 %s", retry, null == time ? "无" : new Date(time));
        result.setMessage(str);
        return result;
    }

    @Around("annotation()")  // 使用上面定义的切入点
    public Object recordLog(ProceedingJoinPoint pjp) throws Throwable {
        if (null == switchOn || switchOn == false) {
            return pjp.proceed();
        }
        if (null == retry) {
            retry = 10 * 1000L;
        }
        Result result = null;
        String methodName = pjp.getSignature().getName();
        Class<?> classTarget = pjp.getTarget().getClass();
        String key = classTarget.getSimpleName() + "." + methodName;
        Long lastTime = map.get(key);
        //如果存在上一次失败时间戳且未过重试时间,则直接返回mock数据,否则尝试再次调用
        if (null != lastTime && lastTime + retry > System.currentTimeMillis()) {
            result = getResult(methodName, classTarget, pjp);
        } else {
            result = (Result) pjp.proceed();
            if (null != result && result.getCode() != HttpStatus.OK.value()) {
                result = getResult(methodName, classTarget, pjp);
                map.put(key, System.currentTimeMillis());
            } else {
                //调用成功则删除时间,下次也直接调用
                map.remove(key);
            }
        }
        return result;
    }

    public void setRetry(Long retry) {
        this.retry = retry;
    }

    public void setSwitchOn(Boolean switchOn) {
        this.switchOn = switchOn;
    }
}