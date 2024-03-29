package zxf.springboot.async.support.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //RequestInterceptor.preHandle在异步场景下同一个Ｈttp请求会调用多次
        MDC.put(TraceConstant.TRACE_ID, TraceIdGenerator.generateTraceId(TraceConstant.WEB_REQUEST));
        logger.info("===> New request, Path : {}", request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //RequestInterceptor.afterCompletion在异步场景下同一个Ｈttp请求会调用多次
        logger.info("<=== End request");
        MDC.remove(TraceConstant.TRACE_ID);
    }
}
