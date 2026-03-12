package zxf.springboot.async.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

/**
 * Spring Security 配置类
 *
 * Spring Boot 3.x / Spring Security 6.x 迁移说明：
 * 1. XML 配置方式已被弃用，改用 Java 配置
 * 2. WebSecurityConfigurerAdapter 已被废弃，使用基于组件的配置
 * 3. authorizeRequests() 改为 authorizeHttpRequests()
 * 4. antMatchers() 改为 requestMatchers()
 * 5. .and() 链式调用不再支持
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置 HTTP 安全规则
     *
     * 迁移说明：
     * - <intercept-url pattern="/**" access="permitAll()"/> → .requestMatchers("/**").permitAll()
     * - <csrf disabled="true"/> → .csrf().disable() (默认在 Spring Security 6 中是启用的)
     * - httpBasic() 启用基本认证
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic
                .authenticationEntryPoint(authenticationEntryPoint())
            );

        return http.build();
    }

    /**
     * 基本认证入口点
     *
     * 对应 XML: <beans:bean id="authenticationEntryPoint" class="...BasicAuthenticationEntryPoint">
     */
    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("My Realm");
        return entryPoint;
    }

    /**
     * 用户详情服务
     *
     * 对应 XML: <user-service><user name="davis" password="{noop}davis" authorities="ROLE_ADMIN"/>
     *
     * 注意：{noop} 表示不使用密码编码器（仅用于演示，生产环境不应使用）
     * Spring Security 6 中推荐使用 BCryptPasswordEncoder
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("davis")
                .password("davis")
                .passwordEncoder(password -> noOpPasswordEncoder().encode(password))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * 密码编码器
     *
     * NoOpPasswordEncoder 已被废弃且不安全，仅用于兼容旧的 XML 配置
     * 生产环境应使用 BCryptPasswordEncoder
     */
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
