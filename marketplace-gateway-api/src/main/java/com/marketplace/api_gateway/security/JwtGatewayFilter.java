package com.marketplace.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.Duration;



@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    public JwtGatewayFilter(JwtUtil jwtUtil, ReactiveRedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();

        // Salta il filtro per il servizio auth
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        // prima controlla la cache
        return checkCache(token, exchange, chain)
                // se non trovato in cache → fallback JWT
                .switchIfEmpty(handleJwtValidation(token, exchange, chain));
    }

    /**
     * Controlla Redis per evitare parsing JWT
     */
    private Mono<Void> checkCache(String token,ServerWebExchange exchange, GatewayFilterChain chain) {

        String cacheKey = "auth:token:" + token;

        return redisTemplate.opsForValue()
                .get(cacheKey)
                .flatMap(cachedData -> {
                    String[] parts = cachedData.split("\\|");
                    String profileId = parts[0];
                    String role = parts[1];
                    String shopId = parts.length > 2 ? parts[2] : "";
                    ServerWebExchange mutatedExchange =
                            buildExchange(exchange, profileId, role, shopId);

                    return chain.filter(mutatedExchange);
                });
    }

    /**
     * Validazione JWT se la cache non contiene il token
     */
    private Mono<Void> handleJwtValidation(String token, ServerWebExchange exchange, GatewayFilterChain chain) {
        try {

            if (!jwtUtil.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            Long profileId = jwtUtil.extractProfileId(token);
            String role = jwtUtil.extractRole(token);
            Long shopId = jwtUtil.extractShopId(token);

            String shopIdStr = shopId != null ? String.valueOf(shopId) : "";

            // salva in cache solo se non esiste già
            String cacheKey = "auth:token:" + token;
            String cacheValue = profileId + "|" + role + "|" + shopIdStr;

            redisTemplate.opsForValue()
                    .get(cacheKey)
                    .flatMap(existing -> {
                        if (existing == null) {
                            return redisTemplate.opsForValue()
                                    .set(cacheKey, cacheValue, Duration.ofHours(24))
                                    .then();
                        }
                        return Mono.empty();
                    })
                    .subscribe();

            ServerWebExchange mutatedExchange = buildExchange(exchange, String.valueOf(profileId),role, shopIdStr);
            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    /**
     * Costruisce la request mutata con gli header per i microservizi
     */
    private ServerWebExchange buildExchange(ServerWebExchange exchange, String profileId, String role, String shopId) {
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-Profile-Id", profileId)
                .header("X-Role", role)
                .header("X-Shop-Id", shopId)
                .build();
        return exchange.mutate()
                .request(mutatedRequest)
                .build();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}