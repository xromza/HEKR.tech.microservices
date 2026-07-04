package ru.xromza.catalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
@RedisHash("Cart")
public class Cart implements Serializable {

    @Id
    private Long userId;

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @TimeToLive
    @Builder.Default
    private Long ttl = 604800L;
}
