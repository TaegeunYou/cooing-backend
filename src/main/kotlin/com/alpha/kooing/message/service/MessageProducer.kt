package com.alpha.kooing.message.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class MessageProducer(
    val kafkaTemplate: KafkaTemplate<String, Any>,
){
    // 여기서는 메세지 produce + 에러 체크까지만 책임이 있음
    fun<T> sendTemplate(topic:String, msg:T):T?{
        val message = MessageBuilder.withPayload(msg as Any)
            .setHeader(KafkaHeaders.TOPIC, topic)
            .setHeader("__TypeId__", msg!!::class.java.name)
            .build()
        println("msg type : ${msg!!::class.java.name}")
        val future = kafkaTemplate.send(message)
        val ret = future.handle { _, error ->
            if(error != null){
                null
            }else{
                msg
            }
        }.get()
        return ret
    }
}