package zxf.springboot.async.support.trace;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ThreadFactory;

public class TraceIdThreadFactory implements ThreadFactory {
    private final ThreadFactory delegate;

    public TraceIdThreadFactory(String threadNamePrefix) {
        delegate = new CustomizableThreadFactory(threadNamePrefix);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return delegate.newThread(RunWIthTraceId.wrap(runnable));
    }
}

