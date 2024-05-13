package com.alpha.kooing.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AmazonS3Config(
    @Value("\${cloud.aws.region.static}")
    val region: String,
    @Value("\${cloud.aws.credentials.access-key}")
    val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}")
    val secretKey: String,
) {

    @Bean
    fun getAmazonS3(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(region)
            .build()
    }


}