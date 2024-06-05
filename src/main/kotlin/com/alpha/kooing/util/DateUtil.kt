package com.alpha.kooing.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object{
        fun getDateTimeFormat(dateTime:LocalDateTime):String{
            return dateTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm"))
        }
    }
}