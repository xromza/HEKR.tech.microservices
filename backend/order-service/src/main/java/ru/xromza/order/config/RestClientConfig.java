package ru.xromza.order.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestClientConfig {

    private ClientHttpRequestFactory defaultRequestFactory() {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(2))
                .withReadTimeout(Duration.ofSeconds(3));
        return ClientHttpRequestFactoryBuilder.detect().build(settings);
    }

    @Bean
    public RestClient orderWorkerRestClient(@Value("${services.order-worker.url}") String orderWorkerUrl) {
        return RestClient.builder()
                .baseUrl(orderWorkerUrl)
                .requestFactory(defaultRequestFactory())
                .build();
    }

    @Bean
    public RestClient catalogRestClient(@Value("${services.catalog.url}") String catalogUrl) {

        return RestClient.builder()
                .baseUrl(catalogUrl)
                .requestFactory(defaultRequestFactory())
                .build();
    }

    @Bean
    public RestClient userRestClient(@Value("${services.user.url}") String userUrl) {
        return RestClient.builder()
                .baseUrl(userUrl)
                .requestFactory(defaultRequestFactory())
                .requestInterceptor((request, body, execution) -> {
                    log.info("Отправляю запрос: {} {}", request.getMethod(), request.getURI());
                    return execution.execute(request, body);
                })
                .build();
    }

    @Bean
    public RestClient warehouseRestClient(@Value("${services.warehouse.url}") String warehouseUrl) {
        return RestClient.builder()
                .baseUrl(warehouseUrl)
                .requestFactory(defaultRequestFactory())
                .build();
    }
}
