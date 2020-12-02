package com.kbds.kbidpassreader.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.toFormatted(): String {
    return SimpleDateFormat("yyyy.MM.dd").format(this)
}