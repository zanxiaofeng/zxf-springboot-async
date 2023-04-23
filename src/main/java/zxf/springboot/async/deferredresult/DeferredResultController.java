package zxf.springboot.async.deferredresult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import zxf.springboot.async.callable.CallableController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/deferred-results")
public class DeferredResultController {
    private static final Logger logger = LoggerFactory.getLogger(DeferredResultController.class);
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @ResponseBody
    @GetMapping("/success")
    public DeferredResult<String> success() {
        logger.info("DeferredResultController::success.start");
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            logger.info("DeferredResultController::success.inner.start");
            result.setResult(LocalDateTime.now().toString());
            logger.info("DeferredResultController::success.inner.end");
        });

        logger.info("DeferredResultController::success.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/error")
    public DeferredResult<String> error() {
        logger.info("DeferredResultController::error.start");
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            logger.info("DeferredResultController::error.inner.start");
            result.setErrorResult(new RuntimeException("DeferredResult error"));
            logger.info("DeferredResultController::error.inner.end");
        });

        logger.info("DeferredResultController::error.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/timeout")
    public DeferredResult<String> timeout() {
        logger.info("DeferredResultController::timeout.start");
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            logger.info("DeferredResultController::timeout.inner.start");
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                logger.info("DeferredResultController::timeout.inner.exception");
                throw new RuntimeException(e);
            }
            result.setResult("DeferredResult timeout");
            logger.info("DeferredResultController::timeout.inner.end");
        });

        logger.info("DeferredResultController::timeout.end");
        return result;
    }

    private DeferredResult<String> createDeferredResult() {
        DeferredResult<String> result = new DeferredResult<>(30 * 1000L);
        result.setResultHandler(str -> {
            logger.info("DeferredResultController.onResult" + ", " + str);
        });
        result.onCompletion(() -> {
            logger.info("DeferredResultController.onCompletion");
        });
        result.onError((error) -> {
            logger.info("DeferredResultController.onError" + ", " + error);
        });
        result.onTimeout(() -> {
            logger.info("DeferredResultController.onTimeout");
        });
        return result;
    }
}
