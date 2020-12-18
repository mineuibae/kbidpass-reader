package com.kbds.kbidpassreader.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kbds.kbidpassreader.data.source.local.dao.LogDao
import com.kbds.kbidpassreader.data.source.local.dao.UserDao
import com.kbds.kbidpassreader.data.source.local.table.LogTable
import com.kbds.kbidpassreader.data.source.local.table.UserTable

@Database(entities = [UserTable::class, LogTable::class], version = 1)
@TypeConverters(Converters::class)
abstract class KBIdPassDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun logDao(): LogDao
}