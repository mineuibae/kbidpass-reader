package com.kbds.kbidpassreader.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate() : Date {
    try {
        return SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(this)
    } catch (e: Exception) {
        throw e
    }
}