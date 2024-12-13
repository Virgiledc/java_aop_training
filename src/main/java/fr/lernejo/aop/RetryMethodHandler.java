package fr.lernejo.aop;

import javassist.util.proxy.MethodHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

public class RetryMethodHandler implements MethodHandler {
    private static final Logger logger = Logger.getLogger(RetryMethodHandler.class.getName());
    private final Object targetObject;

    public RetryMethodHandler(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
        Retry retryAnnotation = method.getAnnotation(Retry.class);

        if (retryAnnotation == null) {
            return executeMethod(method, args);
        }

        return executeWithRetry(method, args, retryAnnotation);
    }

    private Object executeMethod(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(targetObject, args);
        } catch (Exception e) {
            throw e.getCause() != null ? e.getCause() : e;
        }
    }

    private Object executeWithRetry(Method method, Object[] args, Retry retryAnnotation) throws Throwable {
        int remainingAttempts = retryAnnotation.maxTries();
        Exception lastException = null;

        while (remainingAttempts > 0) {
            try {
                return executeMethod(method, args);
            } catch (Exception e) {
                lastException = e;
                if (!shouldRetry(e, retryAnnotation.errorTypes(), remainingAttempts)) {
                    throw e.getCause() != null ? e.getCause() : e;
                }
                logRetryAttempt(method.getName(), retryAnnotation.maxTries() - remainingAttempts + 1);
                remainingAttempts--;
            }
        }

        throw lastException != null ? lastException : new RuntimeException("Max retry attempts reached");
    }

    private boolean shouldRetry(Exception exception, Class<? extends Exception>[] errorTypes, int remainingAttempts) {
        if (remainingAttempts <= 1) {
            return false;
        }

        if (errorTypes.length == 0) {
            return true;
        }

        return Arrays.stream(errorTypes)
            .anyMatch(errorType -> errorType.isInstance(exception.getCause()) || errorType.isInstance(exception));
    }

    private void logRetryAttempt(String methodName, int attemptNumber) {
        logger.warning(String.format("Retrying method %s (Attempt %d)", methodName, attemptNumber));
    }
}
