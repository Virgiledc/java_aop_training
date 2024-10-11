package fr.lernejo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CountingAspect {

    @Pointcut("execution(public * *(..))")
    public void publicMethods() {}

    @After("publicMethods()")
    public void incrementCounter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName();
        InvocationTracker.incrementInvocationCount(methodName);
    }
}
