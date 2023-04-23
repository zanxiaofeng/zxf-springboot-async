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
        logger.info("::success.start");
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            logger.info("::success.inner.start");
            result.setResult(LocalDateTime.now().toString());
            logger.info("::success.inner.end");
        });

        logger.info("::success.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/error")
    public DeferredResult<String> error() {
        logger.info("::error.start");
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            logger.info("::error.inner.start");
            result.setErrorResult(new RuntimeException("DeferredResult error"));
            logger.info("::error.inner.end");
        });

        logger.info("::error.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/timeout")
    public DeferredResult<String> timeout() {
        logger.info("::timeout.start");
        DeferredResult<String> result = createDeferredResult();
        threadPoolTaskExecutor.submit(() -> {
            logger.info("::timeout.inner.start");
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                logger.info("::timeout.inner.exception");
                throw new RuntimeException(e);
            }
            result.setResult("DeferredResult timeout");
            logger.info("::timeout.inner.end");
        });

        logger.info("::timeout.end");
        return result;
    }

    private DeferredResult<String> createDeferredResult() {
        DeferredResult<String> result = new DeferredResult<>(30 * 1000L);
        result.setResultHandler(str -> {
            logger.info("::onResult" + ", " + str);
        });
        result.onCompletion(() -> {
            logger.info("::onCompletion");
        });
        result.onError((error) -> {
            logger.info("::onError" + ", " + error);
        });
        result.onTimeout(() -> {
            logger.info("::onTimeout");
        });
        return result;
    }
}
