package com.alpha.kooing.message.service

import com.alpha.kooing.message.dto.UserMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageProducer(
    val kafkaTemplate: KafkaTemplate<String, UserMessage>,
){
    // 여기서는 메세지 produce + 에러 체크까지만 책임이 있음
    fun sendTemplate(topic:String, msg:UserMessage):Any?{
        val future = kafkaTemplate.send(topic, msg)
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