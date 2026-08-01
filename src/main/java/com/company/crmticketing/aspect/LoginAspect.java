package com.company.crmticketing.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoginAspect {
    @Around("execution(* com.company.crmticketing.service.*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.info("method : {} duration : {} ms", joinPoint.getSignature().getName(), duration);

        if (duration >2000 ) {
            log.warn("slow method process : {} duration : {} ms", joinPoint.getSignature().getName(), duration);
        }
        return result;
    }
}
