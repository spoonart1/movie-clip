package com.spoonart.movieclip.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun toDateObj(fromDateStr: String, pattern: String): Date {
        val sdf = SimpleDateFormat(pattern)
        return sdf.parse(fromDateStr)
    }

    fun toDateStr(fromDateObj: Date, pattern: String): String {
        val sdf = SimpleDateFormat(pattern)
        return sdf.format(fromDateObj)
    }

    fun transformToNewDateStr(dateStr: String, oldPattern: String, newPattern: String): String {
        val date = toDateObj(dateStr, oldPattern)
        return toDateStr(date, newPattern)
    }

}