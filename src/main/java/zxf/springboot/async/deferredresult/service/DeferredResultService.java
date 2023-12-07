package zxf.springboot.async.deferredresult.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class DeferredResultService {
    private static final Logger logger = LoggerFactory.getLogger(DeferredResultService.class);

    public DeferredResult<String> createDeferredResult() {
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
