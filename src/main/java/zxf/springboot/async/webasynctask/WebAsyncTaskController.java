package zxf.springboot.async.webasynctask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import zxf.springboot.async.deferredresult.DeferredResultController;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/web-async-tasks")
public class WebAsyncTaskController {
    private static final Logger logger = LoggerFactory.getLogger(WebAsyncTaskController.class);
    @ResponseBody
    @GetMapping("/success")
    public WebAsyncTask<String> success() {
        logger.info("WebAsyncTaskController::success.start");
        WebAsyncTask<String> result = createWebAsyncTask(() -> {
            logger.info("WebAsyncTaskController::success.inner.start");
            logger.info("WebAsyncTaskController::success.inner.end");
            return LocalDateTime.now().toString();
        });
        logger.info("WebAsyncTaskController::success.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/error")
    public WebAsyncTask<String> error() {
        logger.info("WebAsyncTaskController::error.start");
        WebAsyncTask<String> result = createWebAsyncTask(() -> {
            logger.info("WebAsyncTaskController::error.inner.start");
            throw new RuntimeException("DeferredResult error");
        });
        logger.info("WebAsyncTaskController::error.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/timeout")
    public WebAsyncTask<String> timeout() {
        logger.info("WebAsyncTaskController::timeout.start");
        WebAsyncTask<String> result = createWebAsyncTask(() -> {
            logger.info("WebAsyncTaskController::timeout.inner.start");
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                logger.info("WebAsyncTaskController::timeout.inner.exception");
                throw e;
            }
            logger.info("WebAsyncTaskController::timeout.inner.end");
            return "WebAsyncTask timeout";
        });

        logger.info("WebAsyncTaskController::timeout.end");
        return result;
    }

    private WebAsyncTask<String> createWebAsyncTask(Callable<String> callable) {
        WebAsyncTask<String> result = new WebAsyncTask<>(30 * 1000L, callable);
        result.onCompletion(() -> {
            logger.info("WebAsyncTaskController::onCompletion");
        });
        result.onTimeout(() -> {
            logger.info("WebAsyncTaskController::onTimeout");
            return "WebAsyncTask timeout callback";
        });
        result.onError(() -> {
            logger.info("WebAsyncTaskController::onError");
            return "WebAsyncTask error callback";
        });
        return result;
    }
}
