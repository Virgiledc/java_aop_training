package fr.lernejo.aop;

import javassist.util.proxy.MethodHandler;
import java.lang.reflect.Method;

public class RetryMethodHandler implements MethodHandler {
    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        Retry retry = thisMethod.getAnnotation(Retry.class);
        int maxTries = retry.maxTries();
        Class<? extends Exception>[] errorTypes = retry.errorTypes();

        Throwable lastException = null;
        for (int attempt = 0; attempt < maxTries; attempt++) {
            try {
                return proceed.invoke(self, args);
            } catch (Exception e) {
                if (shouldRetry(e, errorTypes)) {
                    lastException = e;
                } else {
                    throw e;
                }
            }
        }
        throw lastException;
    }

    private boolean shouldRetry(Exception e, Class<? extends Exception>[] errorTypes) {
        if (errorTypes.length == 0) {
            return true;
        }
        for (Class<? extends Exception> errorType : errorTypes) {
            if (errorType.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
}
