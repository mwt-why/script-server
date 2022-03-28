package com.mwt.config.aspect;

import com.mwt.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.mwt.controller..*.*(..))")
    public void logPointcut() {
    }

    @Before("logPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request = HttpUtil.getRequest();
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        Object[] args = joinPoint.getArgs();
        log.info(String.format("method=%s url=%s params=%s", method, url, Arrays.asList(args)));
    }

    @AfterReturning(pointcut = "logPointcut()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = HttpUtil.getRequest();
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        log.info(String.format("method=%s url=%s result=%s", method, url, result));
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "exception")
    public void logThrow(JoinPoint joinPoint, Exception exception) {
        String name = joinPoint.getSignature().getName();
        String message = exception.getMessage();
        log.error(String.format("method=%s throw=%s", name, message));
    }
}
