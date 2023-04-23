package zxf.springboot.async.callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/callables")
public class CallableController {
    private static final Logger logger = LoggerFactory.getLogger(CallableController.class);

    @GetMapping("/success")
    public Callable<String> success() {
        logger.info("CallableController::success.start");
        Callable<String> result = () -> {
            logger.info("CallableController::success.inner.start");
            Thread.sleep(1000);
            logger.info("CallableController::success.inner.end");
            return LocalDateTime.now().toString();
        };

        logger.info("CallableController::success.end");
        return result;
    }

    @GetMapping("/error")
    public Callable<String> error() {
        logger.info("CallableController::error.start");
        Callable<String> result = () -> {
            logger.info("CallableController::error.inner.start");
            throw new RuntimeException("Callable error");
        };

        logger.info("CallableController::error.end");
        return result;
    }

    @GetMapping("/timeout")
    public Callable<String> timeout() {
        logger.info("CallableController::timeout.start");
        Callable<String> result = () -> {
            logger.info("CallableController::timeout.inner.start");
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                logger.info("CallableController::timeout.inner.exception");
                throw e;
            }
            logger.info("CallableController::timeout.inner.end");
            return "Callable timeout";
        };

        logger.info("CallableController::timeout.end");
        return result;
    }
}
