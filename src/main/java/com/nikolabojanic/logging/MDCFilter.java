package com.nikolabojanic.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

public class MDCFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            MDC.put("traceId", UUID.randomUUID().toString());
            MDC.put("httpMethod", ((HttpServletRequest) request).getMethod());
            MDC.put("uri", ((HttpServletRequest) request).getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}

