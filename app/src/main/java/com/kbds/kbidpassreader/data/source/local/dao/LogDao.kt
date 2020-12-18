package com.kbds.kbidpassreader.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kbds.kbidpassreader.data.source.local.table.LogTable

@Dao
interface LogDao {
    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun observeLogs(): LiveData<List<LogTable>>

    @Query("SELECT * FROM logs WHERE id = :id")
    fun observeLog(id: Int): LiveData<LogTable>

    @Query("SELECT * FROM logs WHERE user_id = :user_id ORDER BY id DESC")
    fun observeLogsFromUser(user_id: String): LiveData<List<LogTable>>

    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun getLogs(): List<LogTable>

    @Query("SELECT * FROM logs WHERE id = :id")
    fun getLog(id: Int): LogTable?

    @Query("SELECT * FROM logs WHERE user_id = :user_id ORDER BY id DESC")
    fun getLogsFromUser(user_id: String): List<LogTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLog(log: LogTable)
}