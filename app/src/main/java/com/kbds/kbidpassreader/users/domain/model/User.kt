package com.kbds.kbidpassreader.users.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "pw_hash")
    var pw_hash: String,

    @ColumnInfo(name = "device_id")
    var device_id: String?,

    @ColumnInfo(name = "kb_pass")
    var kb_pass: String?,

    @ColumnInfo(name = "created_at")
    var created_at: Date?
)