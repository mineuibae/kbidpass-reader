package com.kbds.kbidpassreader.domain.model.audit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/*
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
 */
@Entity(tableName = "audits")
data class Audit(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: String? = null,

    @ColumnInfo(name = "user_name")
    var user_name: String? = null,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String? = null,

    @ColumnInfo(name = "desc")
    var desc: String,

    @ColumnInfo(name = "logged_at")
    var logged_at: Date,

    @ColumnInfo(name = "audit_type")
    var audit_type: AuditType
)