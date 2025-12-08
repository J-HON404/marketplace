package com.unicam.cs.progettoweb.marketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Broker semplice di Spring: inoltra messaggi ai client sottoscritti ai topic
        registry.enableSimpleBroker("/topic");
        // Prefisso per inviare messaggi dal client al server (non usato nel tuo caso)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint dove i client si collegano
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
