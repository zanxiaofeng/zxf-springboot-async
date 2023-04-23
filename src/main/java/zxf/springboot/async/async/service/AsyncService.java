package zxf.springboot.async.async.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    @Async
    public CompletableFuture<String> date(String message) throws InterruptedException {
        System.out.println("AsyncService::date.start：" + Thread.currentThread().getName());
        Thread.sleep(10 * 1000L);
        System.out.println("AsyncService::date.end：" + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(LocalDate.now().toString());
    }

    @Async
    public CompletableFuture<String> time(String message) throws InterruptedException {
        System.out.println("AsyncService::time.start：" + Thread.currentThread().getName());
        Thread.sleep(10 * 1000L);
        System.out.println("AsyncService::time.end：" + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(LocalTime.now().toString());
    }

    @Async
    public CompletableFuture<String> timeout(String message) throws InterruptedException {
        System.out.println("AsyncService::timeout.start：" + Thread.currentThread().getName());
        Thread.sleep(35 * 1000L);
        System.out.println("AsyncService::timeout.end：" + Thread.currentThread().getName());
        return CompletableFuture.completedFuture("Async timeout");
    }

    @Async
    public CompletableFuture<String> error(String message) throws InterruptedException {
        System.out.println("AsyncService::error.start：" + Thread.currentThread().getName());
        Thread.sleep(35 * 1000L);
        System.out.println("AsyncService::error.end：" + Thread.currentThread().getName());
        return CompletableFuture.completedFuture(LocalDate.now().toString());
    }
}
