package com.kbds.kbidpassreader.data.source

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.Audit
import com.kbds.kbidpassreader.domain.model.User

interface KBIdPassRepository {

    fun observeUsers() : LiveData<Response<List<User>>>

    fun observeUser(@NonNull id: String) : LiveData<Response<User>>

    suspend fun getUsers() : Response<List<User>>

    suspend fun getUser(@NonNull id: String) : Response<User>

    suspend fun addUser(@NonNull user: User)

    suspend fun deleteUser(@NonNull user: User)

    suspend fun updateUser(@NonNull user: User)

    suspend fun registerUser(@NonNull user: User)

    suspend fun deRegisterUser(@NonNull user: User)

    suspend fun refreshUsers()

    suspend fun refreshUser(@NonNull id: String)


    fun observeAudits() : LiveData<Response<List<Audit>>>

    fun observeAudit(@NonNull id: Int) : LiveData<Response<Audit>>

    fun observeAuditsFromUser(@NonNull user_id: String) : LiveData<Response<List<Audit>>>

    suspend fun getAudits() : Response<List<Audit>>

    suspend fun getAudit(@NonNull id: Int) : Response<Audit>

    suspend fun getAuditsFromUser(@NonNull user_id: String) : Response<List<Audit>>

    suspend fun addAudit(@NonNull audit: Audit)

    suspend fun refreshAudits()
}