package com.alpha.kooing.external

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@Service
class AmazonS3Service(
    private val amazonS3: AmazonS3,
    @Value("\${cloud.aws.s3.bucket-name}")
    private val bucketName: String
) {

    fun upload(
        uploadFile: MultipartFile,
        category: String
    ): String {
        val key = "$category/${UUID.randomUUID().toString().substring(0..10)}.${uploadFile.originalFilename}"
        val objectMataData = ObjectMetadata()
        objectMataData.contentType = uploadFile.contentType
        this.amazonS3.putObject(bucketName, key, uploadFile.inputStream, objectMataData)
        return amazonS3.getUrl(bucketName, key).toString()
    }

    fun download(key: String): ByteArray {
        val responseObject = this.amazonS3.getObject(bucketName, key)
        return responseObject.objectContent.readAllBytes()
    }

    fun remove(key: String) {
        this.amazonS3.deleteObject(bucketName, key)
    }
}