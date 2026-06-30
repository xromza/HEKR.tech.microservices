package ru.xromza.gateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "gateway")
@Getter
@Setter
public class GatewayProperties {
    
    private List<RouteConfig> routes;

    @Getter
    @Setter
    public static class RouteConfig {
        private String path;
        private String uri;
    }
}