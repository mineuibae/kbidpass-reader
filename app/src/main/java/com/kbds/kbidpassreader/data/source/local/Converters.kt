package com.kbds.kbidpassreader.data.source.local

import androidx.room.TypeConverter
import com.kbds.kbidpassreader.domain.model.audit.AuditType
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
    fun fromAuditType(value: AuditType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toAuditType(type: String?): AuditType? {
        return type?.let{ enumValueOf<AuditType>(it) }
    }
}