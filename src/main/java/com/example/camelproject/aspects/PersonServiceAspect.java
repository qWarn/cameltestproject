package com.example.camelproject.aspects;

import com.example.camelproject.exceptions.PersonNotFoundException;
import com.example.camelproject.models.Person;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class PersonServiceAspect {

    @Around(value = "within(com.example.camelproject.sevices.PersonService)")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Started execution of method " + joinPoint.getSignature().getName());
        Object res = joinPoint.proceed();
        log.info("Ended execution of method " + joinPoint.getSignature().getName());

        if (res instanceof Person person){
            log.info("Execution result " + person);
        }

        return res;
    }

    @AfterThrowing(value = "execution(* com.example.camelproject.sevices.PersonService.*(..) " +
            "throws com.example.camelproject.exceptions.PersonNotFoundException)", throwing = "exception")
    public void logUserNotFoundException(JoinPoint joinPoint, PersonNotFoundException exception){
        log.error("Exception with message:" + exception.getMessage() + " | thrown from method: " + joinPoint.getSignature().getName());
    }

}