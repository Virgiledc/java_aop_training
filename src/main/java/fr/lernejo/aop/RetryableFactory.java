package fr.lernejo.aop;

import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.MethodFilter;

public class RetryableFactory {
    public static <T> T buildRetryable(Class<T> clazz) {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(clazz);
        factory.setFilter(new RetryMethodFilter());

        try {
            Class<?> proxyClass = factory.createClass();
            T instance = (T) proxyClass.getDeclaredConstructor().newInstance();
            ((javassist.util.proxy.Proxy) instance).setHandler(new RetryMethodHandler());
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create proxy", e);
        }
    }
}
