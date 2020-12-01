package com.kbds.kbidpassreader.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kbds.kbidpassreader.data.source.local.dao.AuditDao
import com.kbds.kbidpassreader.data.source.local.dao.UserDao
import com.kbds.kbidpassreader.domain.model.Audit
import com.kbds.kbidpassreader.domain.model.User

@Database(entities = [User::class, Audit::class], version = 1)
@TypeConverters(Converters::class)
abstract class KBIdPassDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun auditDao(): AuditDao
}