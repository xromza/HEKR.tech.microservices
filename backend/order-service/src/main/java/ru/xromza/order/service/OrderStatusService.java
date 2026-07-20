package ru.xromza.order.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.xromza.order.utils.Status;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final StringRedisTemplate redisTemplate;
    private final String REDIS_ORDERSTATUS_PREFIX = "orderStatus:";

    public void changeOrderStatus(String orderId, String status) {
        redisTemplate.opsForValue().set(REDIS_ORDERSTATUS_PREFIX + orderId, status, Duration.ofMinutes(30));
    }

    public Status getOrderStatus(String orderId) {
        String value = redisTemplate.opsForValue().get(REDIS_ORDERSTATUS_PREFIX + orderId);
        if (value == null) {
            return Status.PROCESSING;
        }
        return Status.valueOf(value);
    }
}
