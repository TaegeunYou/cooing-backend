package com.alpha.kooing.config.kafka

import com.alpha.kooing.message.dto.UserMessage
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServer: String

    @Bean
    fun producerFactory():ProducerFactory<String, UserMessage>{
        val config: HashMap<String, Any> = hashMapOf()
        config[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        return DefaultKafkaProducerFactory(config, StringSerializer(), JsonSerializer())
    }

    @Bean
    fun kafkaTemplate():KafkaTemplate<String, UserMessage>{
        return KafkaTemplate(producerFactory())
    }
}