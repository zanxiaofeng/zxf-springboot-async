package zxf.springboot.async.webasynctask;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/web-async-tasks")
public class WebAsyncTaskController {
    @ResponseBody
    @GetMapping("/success")
    public WebAsyncTask<String> success() {
        System.out.println("WebAsyncTaskController::success.start：" + Thread.currentThread().getName());
        WebAsyncTask<String> result = createWebAsyncTask(() -> {
            System.out.println("WebAsyncTaskController::success.inner.start：" + Thread.currentThread().getName());
            System.out.println("WebAsyncTaskController::success.inner.end：" + Thread.currentThread().getName());
            return LocalDateTime.now().toString();
        });
        System.out.println("WebAsyncTaskController::success.end：" + Thread.currentThread().getName());
        return result;
    }

    @ResponseBody
    @GetMapping("/error")
    public WebAsyncTask<String> error() {
        System.out.println("WebAsyncTaskController::error.start：" + Thread.currentThread().getName());
        WebAsyncTask<String> result = createWebAsyncTask(() -> {
            System.out.println("WebAsyncTaskController::error.inner.start：" + Thread.currentThread().getName());
            throw new RuntimeException("DeferredResult error");
        });
        System.out.println("WebAsyncTaskController::error.end：" + Thread.currentThread().getName());
        return result;
    }

    @ResponseBody
    @GetMapping("/timeout")
    public WebAsyncTask<String> timeout() {
        System.out.println("WebAsyncTaskController::timeout.start：" + Thread.currentThread().getName());
        WebAsyncTask<String> result = createWebAsyncTask(() -> {
            System.out.println("WebAsyncTaskController::timeout.inner.start：" + Thread.currentThread().getName());
            Thread.sleep(35 * 1000L);
            System.out.println("WebAsyncTaskController::timeout.inner.end：" + Thread.currentThread().getName());
            return "WebAsyncTask timeout";
        });

        System.out.println("WebAsyncTaskController::timeout.end：" + Thread.currentThread().getName());
        return result;
    }

    private WebAsyncTask<String> createWebAsyncTask(Callable<String> callable) {
        WebAsyncTask<String> result = new WebAsyncTask<>(30 * 1000L, callable);
        result.onCompletion(() -> {
            System.out.println("WebAsyncTaskController::onCompletion：" + Thread.currentThread().getName());
        });
        result.onTimeout(() -> {
            System.out.println("WebAsyncTaskController::onTimeout：" + Thread.currentThread().getName());
            return "WebAsyncTask timeout callback";
        });
        result.onError(() -> {
            System.out.println("WebAsyncTaskController::onError：" + Thread.currentThread().getName());
            return "WebAsyncTask error callback";
        });
        return result;
    }
}
