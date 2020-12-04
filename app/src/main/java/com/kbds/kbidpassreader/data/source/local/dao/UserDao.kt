package com.kbds.kbidpassreader.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kbds.kbidpassreader.domain.model.user.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun observeUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUser(id: String): LiveData<User>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteUser(id: String)

    @Update
    fun updateUser(user: User)

    @Query("UPDATE users SET is_registered = :registered WHERE id = :id")
    fun updateRegistered(id: String, registered: Boolean)
}