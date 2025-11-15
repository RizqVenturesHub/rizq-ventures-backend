package com.rizq_venture.rizqconnects.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.rizq_venture.rizqconnects.services..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        log.info("➡START: {}", methodName);

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long time = System.currentTimeMillis() - start;

            log.info(" END: {} ({} ms)", methodName, time);
            return result;

        } catch (Exception ex) {
            log.error("EXCEPTION in {}: {}", methodName, ex.getMessage());
            throw ex;
        }
    }
}
