package com.alpha.kooing.common

import java.time.LocalDate
import java.time.LocalDateTime

object Utils {

    //2024-05-22T12:47:17.554149 -> 05/22 12:47
    fun dateTimeToFrontFormat(dateTime: LocalDateTime): String {
        val str = dateTime.toString()
        return "${str.substring(5..6)}/${str.substring(8..9)} ${str.substring(11..15)}"
    }

    //2024-04-01, 2024-04-23 -> 04.01 ~ 04.23
    fun dateRangeToFrontFormat(startDate: LocalDate, endDate: LocalDate): String {
        val startDateStr = startDate.toString()
        val endDateStr = endDate.toString()
        return "${startDateStr.substring(5..6)}.${endDateStr.substring(8..9)} ~ ${endDateStr.substring(5..6)}.${endDateStr.substring(8..9)}"
    }

}