package zxf.springboot.async.support.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class TraceFilter extends GenericFilterBean {
    private static final Logger slfLogger = LoggerFactory.getLogger(TraceFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MDC.put(TraceConstant.TRACE_ID, TraceIdGenerator.generateTraceId(TraceConstant.WEB_REQUEST));
        slfLogger.info("===> New request, Path : {}", request.getServletContext().getContextPath());
        chain.doFilter(request, response);
        slfLogger.info("<=== End request");
        MDC.remove(TraceConstant.TRACE_ID);
    }
}

