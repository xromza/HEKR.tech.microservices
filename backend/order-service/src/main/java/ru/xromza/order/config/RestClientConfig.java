package ru.xromza.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestClientConfig {

    @Bean
    public RestClient orderWorkerRestClient(@Value("${services.order-worker.url}") String orderWorkerUrl) {
        return RestClient.builder()
                .baseUrl(orderWorkerUrl)
                .build();
    }

    @Bean
    public RestClient catalogRestClient(@Value("${services.catalog.url}") String catalogUrl) {
        return RestClient.builder()
                .baseUrl(catalogUrl)
                .build();
    }

    @Bean
    public RestClient userRestClient(@Value("${services.user.url}") String userUrl) {
        return RestClient.builder()
                .baseUrl(userUrl)
                .requestInterceptor((request, body, execution) -> {
                    log.info("Отправляю запрос: {} {}", request.getMethod(), request.getURI());
                    return execution.execute(request, body);
                })
                .build();
    }
}
