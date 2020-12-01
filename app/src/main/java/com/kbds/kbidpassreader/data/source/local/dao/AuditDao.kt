package com.kbds.kbidpassreader.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kbds.kbidpassreader.domain.model.Audit

@Dao
interface AuditDao {
    @Query("SELECT * FROM audits")
    fun observeAudits(): LiveData<List<Audit>>

    @Query("SELECT * FROM audits WHERE id = :id")
    fun observeAudit(id: Int): LiveData<Audit>

    @Query("SELECT * FROM audits WHERE user_id = :user_id")
    fun observeAuditsFromUser(user_id: String): LiveData<List<Audit>>

    @Query("SELECT * FROM audits")
    fun getAudits(): List<Audit>

    @Query("SELECT * FROM audits WHERE id = :id")
    fun getAudit(id: Int): Audit?

    @Query("SELECT * FROM audits WHERE user_id = :user_id")
    fun getAuditsFromUser(user_id: String): List<Audit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudit(audit: Audit)
}