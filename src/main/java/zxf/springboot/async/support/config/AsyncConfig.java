package zxf.springboot.async.support.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import zxf.springboot.async.support.trace.TraceIdThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        taskExecutor.setKeepAliveSeconds(200);
        taskExecutor.setThreadFactory(new TraceIdThreadFactory("my-pool-for-async-"));
        // 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return customAsyncExceptionHandler();
    }

    private AsyncUncaughtExceptionHandler customAsyncExceptionHandler() {
        return (throwable, method, obj) -> {
            logger.info("::customAsyncExceptionHandler.inner.start");
            logger.info("::customAsyncExceptionHandler.Exception message - " + throwable.getMessage());
            logger.info("::customAsyncExceptionHandler.Method name - " + method.getName());
            for (Object param : obj) {
                logger.info("::customAsyncExceptionHandler.Parameter value - " + param);
            }
        };
    }
}