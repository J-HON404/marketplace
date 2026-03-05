package com.marketplace.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)   // disabilita CSRF
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // disabilita HTTP Basic
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // disabilita login form
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // tutte le rotte libere
                )
                .build();
    }
}