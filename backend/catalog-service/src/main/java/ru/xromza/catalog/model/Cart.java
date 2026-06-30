package ru.xromza.catalog.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Data;

@Data
@RedisHash("Cart")
public class Cart implements Serializable {

    @Id
    private Long userId;

    private List<CartItem> items = new ArrayList<>();

    @TimeToLive
    private Long ttl = 604800L;
}
