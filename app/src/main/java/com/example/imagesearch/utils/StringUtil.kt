package com.example.imagesearch.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object StringUtil {
    fun dateFormat(time:String, format:String):String{
        if(time.isEmpty()) return ""

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val formatter1 = DateTimeFormatter.ofPattern(format)
        val date= LocalDateTime.parse(time, formatter)

        return date.format(formatter1)
    }
}