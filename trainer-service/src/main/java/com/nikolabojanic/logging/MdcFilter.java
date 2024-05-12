package com.nikolabojanic.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;

public class MdcFilter implements Filter {
    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        try {
            String traceId = ((HttpServletRequest) request).getHeader("traceId");
            if (traceId != null) {
                MDC.put("traceId", traceId);
            } else {
                MDC.put("traceId", UUID.randomUUID().toString());
            }
            MDC.put("httpMethod", ((HttpServletRequest) request).getMethod());
            MDC.put("uri", ((HttpServletRequest) request).getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}