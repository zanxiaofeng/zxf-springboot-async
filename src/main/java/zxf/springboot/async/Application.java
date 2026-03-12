package zxf.springboot.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用程序入口类
 *
 * Spring Boot 3.x 升级说明：
 * - 移除了 @ImportResource("classpath:spring-security.xml")
 * - Spring Security 配置已迁移到 SecurityConfig.java
 * - Spring Boot 3.x / Spring Security 6.x 不再推荐使用 XML 配置
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
