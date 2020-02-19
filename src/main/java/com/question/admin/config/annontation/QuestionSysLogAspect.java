package com.question.admin.config.annontation;

import com.alibaba.fastjson.JSONObject;
import com.question.admin.config.shiro.LoginUser;
import com.question.admin.config.shiro.security.UserContext;
import com.question.admin.domain.entity.sysmgr.SysLog;
import com.question.admin.service.sysmgr.SysLogService;
import com.question.admin.utils.DateUtils;
import com.question.admin.utils.IPUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Aspect
@Order(5)
@Component
public class QuestionSysLogAspect {

    @Autowired
    private SysLogService logService;

    public static final String ACCOUNT = "匿名用户";
    private static final String DEFAULT_PKG = "com.question.admin.web";
    private static final String SHORT_PKG = "s.c";

    ThreadLocal<Long> startTime = new ThreadLocal<>();
    ThreadLocal<String> logId = new ThreadLocal<>();

    @Pointcut("@annotation(com.question.admin.config.annontation.SysLogAnnotation)")
    public void questionSysLog() {
    }

    @Before("questionSysLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        startTime.set(System.currentTimeMillis());
        logId.set(UUID.randomUUID().toString());

        LoginUser loginUser = UserContext.getCurrentUser();
        final String method = request.getMethod();
        final String url = request.getRequestURL().toString();
        final String uri = request.getRequestURI();
        final String ip = IPUtils.getIpAddr(request);
        final String account = loginUser != null ? loginUser.getAccount() : ACCOUNT;
        final String clazz = joinPoint.getSignature().getDeclaringTypeName().replaceAll(DEFAULT_PKG, SHORT_PKG);
        final String methodName = joinPoint.getSignature().getName();
        final Object[] args = joinPoint.getArgs();
        final String params = JSONObject.toJSONString(args);
        SysLog sysLog = new SysLog(logId.get(), account, ip, method, url, uri, clazz, methodName, DateUtils.currentDate(),
                0L, params);
        logService.save(sysLog);

    }

    @AfterReturning(returning = "ret", pointcut = "questionSysLog()")
    public void doAfterReturning(Object ret) {
        SysLog sysLog = new SysLog();
        sysLog.setId(logId.get());
        sysLog.setSpendTime( System.currentTimeMillis() - startTime.get());
        logService.updateById(sysLog);
    }
}
