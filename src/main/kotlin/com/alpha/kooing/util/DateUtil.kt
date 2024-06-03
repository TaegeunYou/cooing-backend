package com.alpha.kooing.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object{
        fun getDateTimeFormat(dateTime:LocalDateTime):String{
            val date1 = dateTime.toLocalDate()
            val date2 = LocalDate.now()
            if(date1 != date2){
                return dateTime.format(DateTimeFormatter.ofPattern("MM-dd"))
            }else{
                return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            }
        }
    }
}