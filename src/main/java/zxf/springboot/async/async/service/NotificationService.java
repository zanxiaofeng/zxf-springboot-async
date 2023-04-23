package zxf.springboot.async.async.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Async
    public void notify(String message) throws InterruptedException {
        System.out.println("NotificationService::notify.start：" + Thread.currentThread().getName());
        Thread.sleep(10 * 1000L);
        System.out.println("NotificationService::notify.end：" + Thread.currentThread().getName());
    }
}
