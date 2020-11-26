package com.kbds.kbidpassreader.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kbds.kbidpassreader.logs.domain.model.Logs
import com.kbds.kbidpassreader.data.source.local.log.LogsDao
import com.kbds.kbidpassreader.users.domain.model.User
import com.kbds.kbidpassreader.data.source.local.user.UserDao

@Database(entities = [User::class, Logs::class], version = 1)
@TypeConverters(Converters::class)
abstract class KBIdPassDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun logsDao(): LogsDao

    @Volatile private var INSTANCE: KBIdPassDatabase? = null

    fun getInstance(context: Context): KBIdPassDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(context.applicationContext,
            KBIdPassDatabase::class.java, "KBIdPass.db")
            .build()
}