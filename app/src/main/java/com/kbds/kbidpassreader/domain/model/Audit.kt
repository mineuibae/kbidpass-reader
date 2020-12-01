package com.kbds.kbidpassreader.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "audits",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        )
    ]
)
data class Audit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: String,

    @ColumnInfo(name = "user_name")
    var user_name: String,

    @ColumnInfo(name = "content")
    var content: String?,

    @ColumnInfo(name = "desc")
    var desc: String?,

    @ColumnInfo(name = "accessed_at")
    var accessed_at: Date?
)