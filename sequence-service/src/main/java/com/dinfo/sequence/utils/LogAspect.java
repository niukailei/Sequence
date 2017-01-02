package com.dinfo.sequence.utils;

import com.dinfo.common.model.Response;
import com.google.common.base.Stopwatch;
import com.google.common.base.Throwables;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 日志的切面
 * Created by winston.wang on 2016/11/23.
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * com.dinfo.sequence.web 包下的所有公用方法
     */
    @Pointcut("execution(public * com.dinfo.sequence.web..*.*(..))")
    public void webLog(){

    }

    /**
     * 切点处理（增强）
     * @param joinPoint 切点信息
     * @return 函数处理结果
     */
    @Around("webLog()")
    public Response couponProcess(ProceedingJoinPoint joinPoint){

        LogInfo logInfo=LogInfo.build();
        Stopwatch stopwatch=Stopwatch.createStarted();
        try{
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            logInfo.setUrl(request.getRequestURL().toString())
                    .setHttpMethod(request.getMethod())
                    .setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName())
                    .setIp(request.getRemoteAddr())
                    .setArgs(Arrays.toString(joinPoint.getArgs()))
                    .setResId(request.getParameter("resId"));
            Response res= (Response) joinPoint.proceed();
            logInfo.setResponse(res);
            return res;
        }catch (Throwable e) {
            Signature signature=joinPoint.getSignature();
            String errstack=Throwables.getStackTraceAsString(e);
            logInfo.setErrStack(errstack);
            Response res=Response.notOk(signature.getDeclaringType()+"."+signature.getName()+":"+e.getMessage());
            logInfo.setResponse(res);
            return res;
        }
        finally {
            stopwatch.stop();
            logInfo.setTime(stopwatch.elapsed(TimeUnit.SECONDS));
            logger.info(logInfo.toString());
        }
    }

}