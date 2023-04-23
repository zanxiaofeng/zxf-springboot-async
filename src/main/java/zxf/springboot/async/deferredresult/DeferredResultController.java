package zxf.springboot.async.deferredresult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/deferred-results")
public class DeferredResultController {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @ResponseBody
    @GetMapping("/success")
    public DeferredResult<String> success() {
        System.out.println("DeferredResultController::success.start：" + Thread.currentThread().getName());
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            System.out.println("DeferredResultController::success.inner.start：" + Thread.currentThread().getName());
            result.setResult(LocalDateTime.now().toString());
            System.out.println("DeferredResultController::success.inner.end：" + Thread.currentThread().getName());
        });

        System.out.println("DeferredResultController::success.end：" + Thread.currentThread().getName());
        return result;
    }

    @ResponseBody
    @GetMapping("/error")
    public DeferredResult<String> error() {
        System.out.println("DeferredResultController::error.start：" + Thread.currentThread().getName());
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            System.out.println("DeferredResultController::error.inner.start：" + Thread.currentThread().getName());
            result.setErrorResult(new RuntimeException("DeferredResult error"));
            System.out.println("DeferredResultController::error.inner.end：" + Thread.currentThread().getName());
        });

        System.out.println("DeferredResultController::error.end：" + Thread.currentThread().getName());
        return result;
    }

    @ResponseBody
    @GetMapping("/timeout")
    public DeferredResult<String> timeout() {
        System.out.println("DeferredResultController::timeout.start：" + Thread.currentThread().getName());
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            System.out.println("DeferredResultController::timeout.inner.start：" + Thread.currentThread().getName());
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                System.out.println("DeferredResultController::timeout.inner.exception：" + Thread.currentThread().getName());
                throw new RuntimeException(e);
            }
            result.setResult("DeferredResult timeout");
            System.out.println("DeferredResultController::timeout.inner.end：" + Thread.currentThread().getName());
        });

        System.out.println("DeferredResultController::timeout.end：" + Thread.currentThread().getName());
        return result;
    }

    private DeferredResult<String> createDeferredResult() {
        DeferredResult<String> result = new DeferredResult<>(30 * 1000L);
        result.setResultHandler(str -> {
            System.out.println("DeferredResultController.onResult：" + Thread.currentThread().getName() + ", " + str);
        });
        result.onCompletion(() -> {
            System.out.println("DeferredResultController.onCompletion：" + Thread.currentThread().getName());
        });
        result.onError((error) -> {
            System.out.println("DeferredResultController.onError：" + Thread.currentThread().getName() + ", " + error);
        });
        result.onTimeout(() -> {
            System.out.println("DeferredResultController.onTimeout：" + Thread.currentThread().getName());
        });
        return result;
    }
}
