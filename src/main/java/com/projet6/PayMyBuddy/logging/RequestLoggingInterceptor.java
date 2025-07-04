package com.projet6.PayMyBuddy.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Map;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String queryParams = request.getParameterMap().entrySet().stream()
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining("&"));

        logger.info("==> [{}] {} {} {}", LocalDateTime.now(), request.getMethod(), request.getRequestURI(), queryParams);
        return true;
    }

    // postHandle et afterCompletion sont optionnels, ne pas les déclarer si tu ne t'en sers pas !
}
