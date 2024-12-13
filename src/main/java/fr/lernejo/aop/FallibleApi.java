package fr.lernejo.aop;

public class FallibleApi {

    @Retry(maxTries = 3, errorTypes = {IllegalArgumentException.class})
    public void callWithRetry() throws IllegalArgumentException {
        System.out.println("Trying to execute the method...");
        throw new IllegalArgumentException("Intentional failure");
    }

    public void callWithoutRetry() {
        System.out.println("This method won't retry.");
    }
}
