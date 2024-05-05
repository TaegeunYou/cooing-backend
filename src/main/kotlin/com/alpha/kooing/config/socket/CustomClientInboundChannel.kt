package com.alpha.kooing.config.socket

import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component

@Component
class CustomClientInboundChannel:ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)?:return message
        val senderContainer = accessor.getNativeHeader("sender")
        if(accessor.command==StompCommand.CONNECT && senderContainer!=null){
            val sender = senderContainer[0]
            val principal = UsernamePasswordAuthenticationToken(sender, 0)
            accessor.user = principal
        }
        return message
    }
}