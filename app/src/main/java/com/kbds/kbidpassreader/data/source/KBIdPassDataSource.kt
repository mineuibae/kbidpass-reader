package com.kbds.kbidpassreader.data.source

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.user.User

interface KBIdPassDataSource {

    fun observeUsers() : LiveData<Response<List<User>>>

    fun observeUser(@NonNull id: String) : LiveData<Response<User>>

    suspend fun getUsers() : Response<List<User>>

    suspend fun getUser(@NonNull id: String) : Response<User>

    suspend fun addUser(@NonNull user: User)

    suspend fun deleteUser(@NonNull id: String)

    suspend fun updateUser(@NonNull user: User)

    suspend fun registerUser(@NonNull user: User)

    suspend fun deRegisterUser(@NonNull user: User)

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