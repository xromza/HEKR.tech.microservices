package ru.xromza.user.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;

    private final static String REFRESH_TOKEN_PREFIX = "refresh-token:";
    private final static String USER_ID_PREFIX = "userId:";
    private static final Long TTL_SECONDS = 7 * 24 * 60 * 60L;

    public void saveToken(Long userId, String token) {
        String userKey = USER_ID_PREFIX + userId;
        String tokenKey = REFRESH_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(userKey, token, Expiration.from(TTL_SECONDS, TimeUnit.SECONDS));
        redisTemplate.opsForValue().set(tokenKey, userId.toString(), Expiration.from(TTL_SECONDS, TimeUnit.SECONDS));
    }

    public void revokeToken(String token) {
        String tokenKey = REFRESH_TOKEN_PREFIX + token;
        String userId = redisTemplate.opsForValue().getAndDelete(tokenKey);
        if (userId != null) {
            String userKey = USER_ID_PREFIX + userId;
            redisTemplate.delete(userKey);
        }
    }

    public Optional<Long> findUserByToken(String token) {
        String key = REFRESH_TOKEN_PREFIX + token;
        String userIdStr = redisTemplate.opsForValue().get(key);

        if (userIdStr != null) {
            try {
                return Optional.of(Long.parseLong(userIdStr));
            } catch (NumberFormatException ex) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public void deleteTokenByUserId(Long userId) {
        String key = USER_ID_PREFIX + userId;

        String token = redisTemplate.opsForValue().getAndDelete(key);

        if (token != null) {
            String tokenKey = REFRESH_TOKEN_PREFIX + token;
            redisTemplate.delete(tokenKey);
        }
    }

    public boolean isTokenValid(Long userId, String token) {
        String userKey = USER_ID_PREFIX + userId;
        String stored_token = redisTemplate.opsForValue().get(userKey);
        return token.equals(stored_token);
    }

}
