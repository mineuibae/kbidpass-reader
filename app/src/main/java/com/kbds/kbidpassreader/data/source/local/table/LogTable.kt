package com.kbds.kbidpassreader.data.source.local.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kbds.kbidpassreader.domain.model.log.LogType
import java.util.*

@Entity(tableName = "logs")
data class LogTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: String?,

    @ColumnInfo(name = "user_name")
    var user_name: String?,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String?,

    @ColumnInfo(name = "desc")
    var desc: String,

    @ColumnInfo(name = "logged_at")
    var logged_at: Date,

    @ColumnInfo(name = "log_type")
    var log_type: LogType
)