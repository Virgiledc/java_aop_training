package fr.lernejo.aop;

import javassist.util.proxy.MethodHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RetryMethodHandler implements MethodHandler {
    private final Object target;

    public RetryMethodHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
        Retry retry = method.getAnnotation(Retry.class);
        if (retry == null) {
            return method.invoke(target, args);
        }

        int maxTries = retry.maxTries();
        Class<? extends Exception>[] errorTypes = retry.errorTypes();
        int attempt = 0;

        while (attempt < maxTries) {
            try {
                // Attempt to invoke the method
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Exception && (attempt == maxTries - 1 || !isRetryableException((Exception) cause, errorTypes))) {
                    throw cause; // Rethrow the original exception
                }
                attempt++;
                System.out.println("Retrying... (Attempt " + (attempt + 1) + ")");
            } catch (Exception e) {
                if (attempt == maxTries - 1 || !isRetryableException(e, errorTypes)) {
                    throw e; // Rethrow the original exception
                }
                attempt++;
                System.out.println("Retrying... (Attempt " + (attempt + 1) + ")");
            }
        }
        return null;
    }

    private boolean isRetryableException(Exception e, Class<? extends Exception>[] errorTypes) {
        if (errorTypes.length == 0) {
            return true; // Retry on all exceptions by default
        }

        for (Class<? extends Exception> errorType : errorTypes) {
            if (errorType.isAssignableFrom(e.getClass())) {
                return true;
            }
        }
        return false;
    }
}
