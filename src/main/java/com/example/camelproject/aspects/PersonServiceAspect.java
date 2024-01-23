package com.example.camelproject.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class PersonServiceAspect {

    @Around(value = "within(com.example.camelproject.sevices.PersonService)" +
            "&& args(org.apache.camel.Exchange)")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Started execution of method " + joinPoint.getSignature().getName());
        Object res;

        try {
            res = joinPoint.proceed();
            log.info("Ended execution of method " + joinPoint.getSignature().getName());
        } catch (RuntimeException e) {
            log.warn("Execution of method " + joinPoint.getSignature().getName() + " failed with exception message: \n" +
                    e.getMessage());
            throw e;
        }
        return res;
    }

}