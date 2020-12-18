package com.kbds.kbidpassreader.data.source.local

import androidx.room.TypeConverter
import com.kbds.kbidpassreader.domain.model.log.LogType
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromLogType(value: LogType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toLogType(type: String?): LogType? {
        return type?.let{ enumValueOf<LogType>(it) }
    }
}