package zxf.springboot.async.webasynctask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import zxf.springboot.async.deferredresult.DeferredResultController;
import zxf.springboot.async.webasynctask.service.WebAsyncTaskService;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/web-async-tasks")
public class WebAsyncTaskController {
    private static final Logger logger = LoggerFactory.getLogger(WebAsyncTaskController.class);

    @Autowired
    private WebAsyncTaskService webAsyncTaskService;

    @ResponseBody
    @GetMapping("/success")
    public WebAsyncTask<String> success() {
        logger.info("::success.start");
        WebAsyncTask<String> result = webAsyncTaskService.createWebAsyncTask(() -> {
            logger.info("::success.inner.start");
            logger.info("::success.inner.end");
            return LocalDateTime.now().toString();
        });
        logger.info("::success.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/error")
    public WebAsyncTask<String> error() {
        logger.info("::error.start");
        WebAsyncTask<String> result = webAsyncTaskService.createWebAsyncTask(() -> {
            logger.info("::error.inner.start");
            throw new RuntimeException("DeferredResult error");
        });
        logger.info("::error.end");
        return result;
    }

    @ResponseBody
    @GetMapping("/timeout")
    public WebAsyncTask<String> timeout() {
        logger.info("::timeout.start");
        WebAsyncTask<String> result = webAsyncTaskService.createWebAsyncTask(() -> {
            logger.info("::timeout.inner.start");
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                logger.info("::timeout.inner.exception");
                throw e;
            }
            logger.info("::timeout.inner.end");
            return "WebAsyncTask timeout";
        });

        logger.info("::timeout.end");
        return result;
    }
}
