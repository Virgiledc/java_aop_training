package fr.lernejo.aop;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public class RetryableFactory {
    public static <T> T buildRetryable(Class<T> clazz) throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(clazz);

        proxyFactory.setFilter(new RetryMethodFilter());

        MethodHandler handler = new RetryMethodHandler(clazz.getDeclaredConstructor().newInstance());

        return (T) proxyFactory.create(new Class<?>[0], new Object[0], handler);
    }
}
