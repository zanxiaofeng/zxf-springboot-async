package zxf.springboot.async.async.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Async
    public void notify(String message) throws InterruptedException {
        logger.info("NotificationService::notify.start：" + message);
        Thread.sleep(10 * 1000L);
        logger.info("NotificationService::notify.end：" + message);
    }
}
