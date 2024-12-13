package fr.lernejo;

import fr.lernejo.aop.FallibleApi;
import fr.lernejo.aop.RetryableFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetryTest {

    @Test
    public void testRetryOnAnnotatedMethod() throws Exception {
        FallibleApi api = RetryableFactory.buildRetryable(FallibleApi.class);

        assertThrows(IllegalArgumentException.class, api::callWithRetry);
    }

    @Test
    public void testNoRetryOnNonAnnotatedMethod() throws Exception {
        FallibleApi api = RetryableFactory.buildRetryable(FallibleApi.class);

        api.callWithoutRetry();
    }

    @Test
    public void testRetryOnGeneralException() throws Exception {
        FallibleApi api = RetryableFactory.buildRetryable(FallibleApi.class);

        assertThrows(Exception.class, () -> {
            throw new Exception("General failure");
        });
    }

    @Test
    public void testMaxTriesExhaustion() throws Exception {
        FallibleApi api = RetryableFactory.buildRetryable(FallibleApi.class);

        assertThrows(IllegalArgumentException.class, api::callWithRetry);
    }

    @Test
    public void testEmptyErrorTypes() throws Exception {
        FallibleApi api = RetryableFactory.buildRetryable(FallibleApi.class);

        assertThrows(Exception.class, () -> {
            throw new Exception("General failure");
        });
    }

    @Test
    public void testNonRetryableException() throws Exception {
        FallibleApi api = RetryableFactory.buildRetryable(FallibleApi.class);

        assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("Non-retryable failure");
        });
    }
}


