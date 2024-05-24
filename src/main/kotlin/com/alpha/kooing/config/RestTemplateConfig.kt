package com.alpha.kooing.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset
import java.time.Duration

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(60000))    //연결 타임아웃
            .setReadTimeout(Duration.ofMillis(300000))      //읽기 타임아웃
            .additionalMessageConverters(StringHttpMessageConverter(Charset.forName("UTF-8")))
            .build()
    }
}