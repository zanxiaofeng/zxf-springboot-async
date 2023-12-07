package zxf.springboot.async.support.trace;

import org.apache.catalina.core.ApplicationFilterChain;
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
//This servlet filter will be the first filter
public class TraceFilter extends GenericFilterBean {
    private static final Logger slfLogger = LoggerFactory.getLogger(TraceFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (chain instanceof ApplicationFilterChain) {
            ApplicationFilterChain myChain = (ApplicationFilterChain)chain;
            //You can see servlet filter list at this place by debugger
            //1.ApplicationFilterConfig[name=traceFilter, filterClass=zxf.springboot.async.support.trace.TraceFilter]
            //2.ApplicationFilterConfig[name=characterEncodingFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter]
            //3.ApplicationFilterConfig[name=formContentFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedFormContentFilter]
            //4.ApplicationFilterConfig[name=requestContextFilter, filterClass=org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter]
            //5.ApplicationFilterConfig[name=springSecurityFilterChain, filterClass=org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean$1]
            //6.ApplicationFilterConfig[name=org.springframework.security.filterChainProxy, filterClass=org.springframework.security.web.FilterChainProxy]
            //7.ApplicationFilterConfig[name=org.springframework.security.web.access.intercept.FilterSecurityInterceptor#0, filterClass=org.springframework.security.web.access.intercept.FilterSecurityInterceptor]
            //8.ApplicationFilterConfig[name=Tomcat WebSocket (JSR356) Filter, filterClass=org.apache.tomcat.websocket.server.WsFilter]

            //You can also see spring security filters in springSecurityFilterChain
            //FilterChainProxy[Filter Chains: [DefaultSecurityFilterChain [RequestMatcher=any request, Filters=[org.springframework.security.web.context.SecurityContextPersistenceFilter@4b2a30d, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@322803db, org.springframework.security.web.header.HeaderWriterFilter@56ba8773, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@6ceb7b5e, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@7dd00705, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@f14e5bf, org.springframework.security.web.session.SessionManagementFilter@d176a31, org.springframework.security.web.access.ExceptionTranslationFilter@3a91d146, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@4784013e]]]]
        }

        MDC.put(TraceConstant.TRACE_ID, TraceIdGenerator.generateTraceId(TraceConstant.WEB_REQUEST));
        slfLogger.info("===> New request, Path : {}, {}", request.getServletContext().getContextPath(), chain);
        chain.doFilter(request, response);
        slfLogger.info("<=== End request");
        MDC.remove(TraceConstant.TRACE_ID);
    }
}

