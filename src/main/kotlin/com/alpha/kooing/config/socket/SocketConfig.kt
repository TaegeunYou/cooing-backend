package com.alpha.kooing.config.socket

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class SocketConfig(
    val clientInboundChannel: CustomClientInboundChannel
):WebSocketMessageBrokerConfigurer {
    @Value(value = "\${spring.cors.allowed-origin}")
    lateinit var allowedOrigin:String
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOrigins(allowedOrigin)
            .withSockJS()
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // MessageMapping 매핑 prefix
        registry.setApplicationDestinationPrefixes("/app")
        // topic:여러 사용자에게 메세지 전송시 사용 (공지)
        // queue:발행자에게 메세지 재전송시 사용
        registry.enableSimpleBroker("/topic", "/queue")
        // 특정 사용자에게 메세지 전송시 사용
        registry.setUserDestinationPrefix("/user")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(clientInboundChannel)
    }
}