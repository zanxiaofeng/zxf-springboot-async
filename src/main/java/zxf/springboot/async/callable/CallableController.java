package zxf.springboot.async.callable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/callables")
public class CallableController {

    @GetMapping("/success")
    public Callable<String> success() {
        System.out.println("CallableController::success.start：" + Thread.currentThread().getName());
        Callable<String> result = () -> {
            System.out.println("CallableController::success.inner.start：" + Thread.currentThread().getName());
            Thread.sleep(1000);
            System.out.println("CallableController::success.inner.end：" + Thread.currentThread().getName());
            return LocalDateTime.now().toString();
        };

        System.out.println("CallableController::success.end：" + Thread.currentThread().getName());
        return result;
    }

    @GetMapping("/error")
    public Callable<String> error() {
        System.out.println("CallableController::error.start：" + Thread.currentThread().getName());
        Callable<String> result = () -> {
            System.out.println("CallableController::error.inner.start：" + Thread.currentThread().getName());
            throw new RuntimeException("Callable error");
        };

        System.out.println("CallableController::error.end：" + Thread.currentThread().getName());
        return result;
    }

    @GetMapping("/timeout")
    public Callable<String> timeout() {
        System.out.println("CallableController::timeout.start：" + Thread.currentThread().getName());
        Callable<String> result = () -> {
            System.out.println("CallableController::timeout.inner.start：" + Thread.currentThread().getName());
            try {
                Thread.sleep(35 * 1000L);
            } catch (InterruptedException e) {
                System.out.println("CallableController::timeout.inner.exception：" + Thread.currentThread().getName());
                throw e;
            }
            System.out.println("CallableController::timeout.inner.end：" + Thread.currentThread().getName());
            return "Callable timeout";
        };

        System.out.println("CallableController::timeout.end：" + Thread.currentThread().getName());
        return result;
    }
}
