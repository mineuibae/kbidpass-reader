package com.kbds.kbidpassreader.logs.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kbds.kbidpassreader.users.domain.model.User
import java.util.*

@Entity(
    tableName = "logs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        )
    ]
)
data class Logs(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    var index: Int = 0,

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "content")
    var content: String?,

    @ColumnInfo(name = "desc")
    var desc: String?,

    @ColumnInfo(name = "accessed_at")
    var accessed_at: Date?
)