package zxf.springboot.async.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.tags.EditorAwareTag;
import zxf.springboot.async.async.service.AsyncService;
import zxf.springboot.async.async.service.NotificationService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
public class AsyncController {
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private NotificationService notificationService;

    @ResponseBody
    @GetMapping("/success")
    public String success() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("AsyncController::success.start：" + Thread.currentThread().getName());
        CompletableFuture<String> date = asyncService.date("date");
        CompletableFuture<String> time = asyncService.time("time");
        CompletableFuture completableFuture = CompletableFuture.allOf(date, time);
        completableFuture.get(30, TimeUnit.SECONDS);
        notificationService.notify("success");
        System.out.println("AsyncController::success.end：" + Thread.currentThread().getName());
        return date.get() + "T" + time.get();
    }


    @ResponseBody
    @GetMapping("/timeout")
    public String timeout() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("AsyncController::timeout.start：" + Thread.currentThread().getName());
        CompletableFuture<String> date = asyncService.date("date");
        CompletableFuture<String> time = asyncService.time("time");
        CompletableFuture<String> timeout = asyncService.timeout("timeout");
        CompletableFuture completableFuture = CompletableFuture.allOf(date, time, timeout);
        completableFuture.get(30, TimeUnit.SECONDS);
        System.out.println("AsyncController::timeout.end：" + Thread.currentThread().getName());
        return date.get() + "T" + time.get();
    }
}
