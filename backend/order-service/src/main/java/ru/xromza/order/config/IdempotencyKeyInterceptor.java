package ru.xromza.order.config;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class IdempotencyKeyInterceptor implements HandlerInterceptor {
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String key = request.getHeader("Idempotency-Key");

        if (key == null || key.isBlank()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Idempotency-Key header");
            return false;
        }

        if (!UUID_PATTERN.matcher(key).matches()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Idempotency-Key format. Must be a valid UUID.");
            return false;
        }

        return true;
    }
}
