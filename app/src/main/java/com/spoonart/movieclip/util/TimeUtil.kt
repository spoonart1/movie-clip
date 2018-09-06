package com.spoonart.movieclip.util

import java.text.SimpleDateFormat

object TimeUtil {
    fun isExpired(dateStr: String): Boolean {
        val sixtyMinutesMillis: Long = 59 * 60 * 1000
        val sixtyAgo = System.currentTimeMillis() - sixtyMinutesMillis
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
        val endDate = format.parse(dateStr)

        return endDate.time < sixtyAgo
    }
}