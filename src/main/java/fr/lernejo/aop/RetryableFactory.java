package fr.lernejo.aop;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public class RetryableFactory {
    public static <T> T buildRetryable(Class<T> clazz) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(clazz);

        // Set the method filter to only handle methods annotated with @Retry
        proxyFactory.setFilter(new RetryMethodFilter());

        // Create the MethodHandler that will handle method invocations and retries
        MethodHandler handler = new RetryMethodHandler(clazz.getDeclaredConstructor().newInstance());

        // Create and return the proxy instance
        return (T) proxyFactory.create(new Class<?>[0], new Object[0], handler);
    }
}
