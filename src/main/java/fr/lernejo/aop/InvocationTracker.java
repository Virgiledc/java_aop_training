package fr.lernejo.aop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvocationTracker {
    private static final Map<String, Long> invocationCounts = new ConcurrentHashMap<>();

    public static List<String> detectedMethods() {
        return new ArrayList<>(invocationCounts.keySet());
    }

    public static long getInvocationCount(String methodQualifiedName) {
        return invocationCounts.getOrDefault(methodQualifiedName, 0L);
    }

    public static void incrementInvocationCount(String methodQualifiedName) {
        invocationCounts.merge(methodQualifiedName, 1L, Long::sum);
    }
}
