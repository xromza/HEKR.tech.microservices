package ru.xromza.gateway.utils;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.xromza.gateway.config.GatewayProperties;

@Component
@RequiredArgsConstructor
public class GatewayFilter implements WebFilter {
    private final GatewayProperties gatewayProperties;
    private final WebClient webClient;
    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final List<String> openPaths = List.of(
            "/api/v1/users/login",
            "/api/v1/users/register",
            "/api/v1/catalog/**",
            "/api/v1/warehouse/**",
            "/api/v1/stock/**"
        );

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String targetTargetUri = gatewayProperties
                .getRoutes()
                .stream()
                .filter(route -> pathMatcher.match(route.getPath(),path))
                .map(GatewayProperties.RouteConfig::getUri)
                .findFirst()
                .orElse(null);

        if (targetTargetUri == null) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().setComplete();
        }

        boolean isSecured = openPaths.stream().noneMatch(pattern -> pathMatcher.match(pattern, path));

        String userId = null;
        String role = null;

        if (isSecured) {
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            if (jwtUtil.isTokenInvalid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            Claims claims = jwtUtil.extractAllClaims(token);
            userId = String.valueOf(claims.get("userId"));
            role = String.valueOf(claims.get("role"));
        }
        String forwardUrl = targetTargetUri + path;
        if (request.getURI().getQuery() != null) {
            forwardUrl += "?" + request.getURI().getQuery();
        }

        final String finalUserId = userId;
        final String finalRole = role;
        return webClient.method(request.getMethod())
                .uri(URI.create(forwardUrl))
                .headers(headers -> {
                    headers.addAll(request.getHeaders());

                    if (finalUserId != null && !finalUserId.equals("null")) {
                        headers.set("X-User-Id", finalUserId);
                        headers.set("X-User-Roles", finalRole);
                    }
                })
                .body(request.getBody(), org.springframework.core.io.buffer.DataBuffer.class)
                .exchangeToMono(clientResponse -> {
                    exchange.getResponse().setStatusCode(clientResponse.statusCode());
                    exchange.getResponse().getHeaders().addAll(clientResponse.headers().asHttpHeaders());

                    return exchange.getResponse().writeWith(
                            clientResponse.bodyToFlux(org.springframework.core.io.buffer.DataBuffer.class));
                });
    }
}
