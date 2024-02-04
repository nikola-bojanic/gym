package com.nikolabojanic.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MdcFilter implements Filter {
    private String traceId;

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {
        try {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
            MDC.put("httpMethod", ((HttpServletRequest) request).getMethod());
            MDC.put("uri", ((HttpServletRequest) request).getRequestURI());
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}

