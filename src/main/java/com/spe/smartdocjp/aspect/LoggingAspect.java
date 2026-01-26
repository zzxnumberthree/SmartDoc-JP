package com.spe.smartdocjp.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// 作用：利用 AOP 在 Service 层方法执行前后自动打印日志，实现业务逻辑与日志记录解耦。
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.spe.smartdocjp.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取方法签名信息
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(">> [START] {}.{}", className, methodName);

        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            // 记录异常日志，不仅包含消息，最好包含堆栈（此处简化为消息）
            logger.error("!! [Exception] {}.{}: Error: {}", className, methodName, ex.getMessage());
            // 必须重新抛出异常，否则 Controller 层会认为方法执行成功并收到 null 返回值
            throw ex;
        }

        long duration = System.currentTimeMillis() - startTime;
        logger.info("<< [END] {}.{}() Time: {} ms", className, methodName, duration);

        return result;
    }
}