package com.alpha.kooing.config.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaTopicConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServer: String

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val config:HashMap<String, Any> = hashMapOf()
        config[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        return KafkaAdmin(config)
    }

    @Bean
    fun chatTopic(): NewTopic {
        return TopicBuilder.name("chat").build()
    }

    @Bean
    fun chattingTopic(): NewTopic {
        return TopicBuilder.name("chatting").build()
    }
}