package com.alpha.kooing.config.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig{
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServer: String

    @Bean
    fun consumerFactory():ConsumerFactory<String, Any>{
        // consumerFactory 객체 생성
        val config:HashMap<String, Any> = hashMapOf()
        config[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServer
        return DefaultKafkaConsumerFactory(config, StringDeserializer(), CustomJsonDeserializer<Any>())
    }

    @Bean
    fun kafkaListenerContainerFactory():ConcurrentKafkaListenerContainerFactory<String, Any>{
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}