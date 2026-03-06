package com.unicam.cs.progettoweb.marketplace.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro JWT intelligente:
 * 1) Se le info principali (profileId, role, shopId) vengono passate dal gateway negli header,
 *    crea un Authentication leggero senza fare query al DB.
 * 2) Se non ci sono, legge il JWT dall'Authorization header,
 *    verifica firma e scadenza, e blocca con 401 se non valido.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //controllo se il gateway ha passato gli header
        String gatewayProfileId = request.getHeader("X-Profile-Id");
        String gatewayRole = request.getHeader("X-Role");
        String gatewayShopId = request.getHeader("X-Shop-Id");

        if (gatewayProfileId != null && gatewayRole != null) {
            // Authentication leggero direttamente dalle info del gateway
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + gatewayRole);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            new JwtPrincipal(Long.parseLong(gatewayProfileId),
                                    gatewayRole,
                                    gatewayShopId != null ? Long.parseLong(gatewayShopId) : null),
                            null,
                            Collections.singletonList(authority)
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            return;
        }
        // legge JWT dall'header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (SecurityContextHolder.getContext().getAuthentication() == null &&
                        jwtUtil.isTokenValid(token, username)) {
                    // Carica utente dal DB
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                    return; // token valido → request continua
                }
            } catch (Exception e) {
                logger.warn("JWT validation failed: " + e.getMessage());
            }
        }
        // token assente o non valido → blocca con 401
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("Unauthorized: Invalid or missing JWT token");
        response.getWriter().flush();
    }
    /*** Classe interna per rappresentare principal “leggero” basato sulle info del gateway.**/
    public static class JwtPrincipal {
        private final Long profileId;
        private final String role;
        private final Long shopId;
        public JwtPrincipal(Long profileId, String role, Long shopId) {
            this.profileId = profileId;
            this.role = role;
            this.shopId = shopId;
        }
        public Long getProfileId() { return profileId; }
        public String getRole() { return role; }
        public Long getShopId() { return shopId; }
    }
}
