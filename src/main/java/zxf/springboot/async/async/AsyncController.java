package zxf.springboot.async.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.tags.EditorAwareTag;
import zxf.springboot.async.async.service.AsyncService;
import zxf.springboot.async.async.service.NotificationService;
import zxf.springboot.async.deferredresult.DeferredResultController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/async")
public class AsyncController {
    private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private NotificationService notificationService;

    @ResponseBody
    @GetMapping("/success")
    public String success() throws InterruptedException, ExecutionException, TimeoutException {
        logger.info("AsyncController::success.start");
        CompletableFuture<String> date = asyncService.date("date");
        CompletableFuture<String> time = asyncService.time("time");
        CompletableFuture completableFuture = CompletableFuture.allOf(date, time);
        completableFuture.get(30, TimeUnit.SECONDS);
        notificationService.notify("success");
        logger.info("AsyncController::success.end");
        return "Success-" + date.get() + "T" + time.get();
    }


    @ResponseBody
    @GetMapping("/timeout")
    public String timeout() throws InterruptedException, ExecutionException, TimeoutException {
        logger.info("AsyncController::timeout.start");
        CompletableFuture<String> date = asyncService.date("date");
        CompletableFuture<String> time = asyncService.time("time");
        CompletableFuture<String> timeout = asyncService.timeout("timeout");
        CompletableFuture completableFuture = CompletableFuture.allOf(date, time, timeout);
        completableFuture.get(30, TimeUnit.SECONDS);
        notificationService.notify("timeout");
        logger.info("AsyncController::timeout.end");
        return "Timeout-" + date.get() + "T" + time.get() + "-" + timeout.get();
    }

    @ResponseBody
    @GetMapping("/error")
    public String error() throws InterruptedException, ExecutionException, TimeoutException {
        logger.info("AsyncController::error.start");
        CompletableFuture<String> date = asyncService.date("date");
        CompletableFuture<String> time = asyncService.time("time");
        CompletableFuture<String> error = asyncService.error("error");
        CompletableFuture completableFuture = CompletableFuture.allOf(date, time, error);
        completableFuture.get(30, TimeUnit.SECONDS);
        notificationService.notify("timeout");
        logger.info("AsyncController::error.end");
        return "Error-" + date.get() + "T" + time.get() + "-" + error.get();
    }
}
