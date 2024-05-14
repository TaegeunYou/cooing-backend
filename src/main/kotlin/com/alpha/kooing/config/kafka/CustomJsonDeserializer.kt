package com.alpha.kooing.config.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.header.Headers
import org.springframework.kafka.support.serializer.JsonDeserializer

class CustomJsonDeserializer<T>:JsonDeserializer<T>() {
    @Suppress("UNCHECKED_CAST")
    override fun deserialize(topic: String?, headers: Headers?, data: ByteArray?): T? {
        val typeName = headers?.lastHeader("__TypeId__")?.let { String(it.value()) }
        val objectMapper = jacksonObjectMapper()
        return try {
            val clazz = Class.forName(typeName) as Class<T>
            val obj = objectMapper.readValue(data, clazz)
            obj
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}