package zxf.springboot.async.deferredresult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/deferred-results")
public class DeferredResultController {
    private DeferredResult<String> result;

    @ResponseBody
    @GetMapping("/start")
    public DeferredResult<String> start() {
        System.out.println("DeferredResultController::start.start：" + Thread.currentThread().getName());
        result = new DeferredResult<>(30 * 1000L);
        result.setResultHandler(str->{
            System.out.println("DeferredResultController.onResult：" + Thread.currentThread().getName() + ", " + str);
        });
        result.onCompletion(() -> {
            System.out.println("DeferredResultController.onCompletion：" + Thread.currentThread().getName());
        });
        result.onError((error)->{
            System.out.println("DeferredResultController.onError：" + Thread.currentThread().getName() + ", " + error);
        });
        result.onTimeout(()->{
            System.out.println("DeferredResultController.onTimeout：" + Thread.currentThread().getName());
        });
        System.out.println("DeferredResultController::start.end：" + Thread.currentThread().getName());
        return result;
    }

    @GetMapping("/error")
    public void error() {
        System.out.println("DeferredResultController::error.start：" + Thread.currentThread().getName());
        result.setErrorResult(new RuntimeException("DeferredResult error"));
        System.out.println("DeferredResultController::error.end：" + Thread.currentThread().getName());
    }

    @GetMapping("/finish")
    public void finish() {
        System.out.println("DeferredResultController::finish.start：" + Thread.currentThread().getName());
        result.setResult(LocalDateTime.now().toString());
        System.out.println("DeferredResultController::finish.end：" + Thread.currentThread().getName());
    }
}
