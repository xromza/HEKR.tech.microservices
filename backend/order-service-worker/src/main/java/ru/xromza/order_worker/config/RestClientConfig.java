package ru.xromza.order_worker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient userRestClient(@Value("${services.user.url}") String userServiceUrl) {
        return RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    @Bean
    public RestClient catalogRestClient(@Value("${services.catalog.url}") String catalogServiceUrl) {
        return RestClient.builder()
                .baseUrl(catalogServiceUrl)
                .build();
    }
}
