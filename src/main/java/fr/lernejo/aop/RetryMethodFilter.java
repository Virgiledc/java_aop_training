package fr.lernejo.aop;

import javassist.util.proxy.MethodFilter;

import java.lang.reflect.Method;

public class RetryMethodFilter implements MethodFilter {
    @Override
    public boolean isHandled(Method method) {
        // Filter methods annotated with @Retry
        return method.isAnnotationPresent(Retry.class);
    }
}
