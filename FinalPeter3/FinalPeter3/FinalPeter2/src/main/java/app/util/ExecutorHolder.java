package app.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ExecutorHolder {
    private static final ExecutorService EXEC =
            Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2));

    private ExecutorHolder() {}

    public static ExecutorService get() {
        return EXEC;
    }
}
