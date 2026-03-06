package com.marketplace.api_gateway.security;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro JWT  API Gateway.
 * - Blocca la request se JWT non valido
 * - Estrae claims principali se valido
 * - Invia header X-Profile-Id, X-Role, X-Shop-Id ai microservizi
 */
@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtGatewayFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Token assente → blocca con 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            // Se il token non è valido → blocca con 401
            if (!jwtUtil.isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Token valido → estrai claims
            Long profileId = jwtUtil.extractProfileId(token);
            String role = jwtUtil.extractRole(token);
            Long shopId = jwtUtil.extractShopId(token);

            // Aggiungi header ai microservizi
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Profile-Id", String.valueOf(profileId))
                    .header("X-Role", role)
                    .header("X-Shop-Id", shopId != null ? String.valueOf(shopId) : "")
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            // Qualsiasi eccezione → blocca la request
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // esegue prima degli altri filtri
    }
}