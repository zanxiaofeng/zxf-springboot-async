package zxf.springboot.async.async.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import zxf.springboot.async.async.AsyncController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);
    @Async
    public CompletableFuture<String> date(String message) throws InterruptedException {
        logger.info("::date.start");
        Thread.sleep(10 * 1000L);
        logger.info("::date.end");
        return CompletableFuture.completedFuture(LocalDate.now().toString());
    }

    @Async
    public CompletableFuture<String> time(String message) throws InterruptedException {
        logger.info("::time.start");
        Thread.sleep(10 * 1000L);
        logger.info("::time.end");
        return CompletableFuture.completedFuture(LocalTime.now().toString());
    }

    @Async
    public CompletableFuture<String> timeout(String message) throws InterruptedException {
        logger.info("::timeout.start");
        try {
            Thread.sleep(35 * 1000L);
        } catch (InterruptedException e) {
            logger.info("::timeout.exception");
            throw e;
        }
        logger.info("::timeout.end");
        return CompletableFuture.completedFuture("Async timeout");
    }

    @Async
    public CompletableFuture<String> error(String message) throws InterruptedException {
        logger.info("::error.start");
        Thread.sleep(10 * 1000L);
        throw new RuntimeException("Async error");
    }
}
