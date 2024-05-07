package com.alpha.kooing.common.handler

import com.alpha.kooing.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleCommonException(e: Exception): ResponseEntity<Any> {
        e.printStackTrace()
        return ResponseEntity.ok().body(
            ApiResponse.error(e.message)
        )
    }
}