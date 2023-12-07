package zxf.springboot.async.webasynctask.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.WebAsyncTask;
import zxf.springboot.async.webasynctask.WebAsyncTaskController;

import java.util.concurrent.Callable;

@Service
public class WebAsyncTaskService {
    private static final Logger logger = LoggerFactory.getLogger(WebAsyncTaskService.class);

    public WebAsyncTask<String> createWebAsyncTask(Callable<String> callable) {
        WebAsyncTask<String> result = new WebAsyncTask<>(30 * 1000L, callable);
        result.onCompletion(() -> {
            logger.info("::onCompletion");
        });
        result.onTimeout(() -> {
            logger.info("::onTimeout");
            return "WebAsyncTask timeout callback";
        });
        result.onError(() -> {
            logger.info("::onError");
            return "WebAsyncTask error callback";
        });
        return result;
    }
}
