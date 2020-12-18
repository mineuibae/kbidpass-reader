package com.kbds.kbidpassreader.data.source

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.user.UserEntity

interface KBIdPassDataSource {

    fun observeUsers() : LiveData<Response<List<UserEntity>>>

    fun observeUser(@NonNull id: String) : LiveData<Response<UserEntity>>

    suspend fun getUsers() : Response<List<UserEntity>>

    suspend fun getUser(@NonNull id: String) : Response<UserEntity>

    suspend fun addUser(@NonNull user: UserEntity)

    suspend fun deleteUser(@NonNull id: String)

    suspend fun updateUser(@NonNull user: UserEntity)

    suspend fun registerUser(@NonNull user: UserEntity)

    suspend fun deRegisterUser(@NonNull user: UserEntity)

    suspend fun refreshUsers()

    suspend fun refreshUser(@NonNull id: String)


    fun observeLogs() : LiveData<Response<List<LogEntity>>>

    fun observeLog(@NonNull id: Int) : LiveData<Response<LogEntity>>

    fun observeLogsFromUser(@NonNull user_id: String) : LiveData<Response<List<LogEntity>>>

    suspend fun getLogs() : Response<List<LogEntity>>

    suspend fun getLog(@NonNull id: Int) : Response<LogEntity>

    suspend fun getLogsFromUser(@NonNull user_id: String) : Response<List<LogEntity>>

    suspend fun addLog(@NonNull log: LogEntity)

    suspend fun refreshLogs()
}