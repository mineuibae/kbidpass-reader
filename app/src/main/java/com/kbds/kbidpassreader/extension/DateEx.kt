package com.kbds.kbidpassreader.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormatted(): String {
    return SimpleDateFormat("yyyy.MM.dd").format(this)
}

fun Date.toFormatted(pattern: String): String {
    return SimpleDateFormat(pattern).format(this)
}