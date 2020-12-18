package com.kbds.kbidpassreader.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kbds.kbidpassreader.data.source.local.table.UserTable

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun observeUsers(): LiveData<List<UserTable>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUser(id: String): LiveData<UserTable>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): List<UserTable>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: String): UserTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserTable)

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteUser(id: String)

    @Update
    fun updateUser(user: UserTable)

    @Query("UPDATE users SET is_registered = :registered WHERE id = :id")
    fun updateRegistered(id: String, registered: Boolean)
}