package com.kbds.kbidpassreader.data.source.local.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "pw_hash")
    var pw_hash: String,

    @ColumnInfo(name = "device_id")
    var device_id: String? = null,

    @ColumnInfo(name = "kb_pass")
    var kb_pass: String? = null,

    @ColumnInfo(name = "created_at")
    var created_at: Date? = null,

    @ColumnInfo(name = "registered_at")
    var registered_at: Date? = null,

    @ColumnInfo(name = "is_registered")
    var is_registered: Boolean = false
)