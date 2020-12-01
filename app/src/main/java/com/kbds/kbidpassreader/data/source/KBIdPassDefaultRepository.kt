package com.kbds.kbidpassreader.data.source

import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.Audit
import com.kbds.kbidpassreader.domain.model.User
import kotlinx.coroutines.*

class KBIdPassDefaultRepository(
    private val kbIdPassLocalDataSource: KBIdPassDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : KBIdPassRepository {

    override fun observeUsers(): LiveData<Response<List<User>>> {
        return kbIdPassLocalDataSource.observeUsers()
    }

    override fun observeUser(id: String): LiveData<Response<User>> {
        return kbIdPassLocalDataSource.observeUser(id)
    }

    override suspend fun getUsers(): Response<List<User>> {
        return kbIdPassLocalDataSource.getUsers()
    }

    override suspend fun getUser(id: String): Response<User> {
        return kbIdPassLocalDataSource.getUser(id)
    }

    override suspend fun addUser(user: User) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.addUser(user) }
        }
    }

    override suspend fun deleteUser(user: User) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.deleteUser(user) }
        }
    }

    override suspend fun updateUser(user: User) {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { kbIdPassLocalDataSource.updateUser(user) }
            }
        }
    }

    override suspend fun registerUser(user: User) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.registerUser(user) }
        }
    }

    override suspend fun deRegisterUser(user: User) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.deRegisterUser(user) }
        }
    }

    override suspend fun refreshUsers() {
        kbIdPassLocalDataSource.refreshUsers()
    }

    override suspend fun refreshUser(id: String) {
        kbIdPassLocalDataSource.refreshUser(id)
    }

    override fun observeAudits(): LiveData<Response<List<Audit>>> {
        return kbIdPassLocalDataSource.observeAudits()
    }

    override fun observeAudit(id: Int): LiveData<Response<Audit>> {
        return kbIdPassLocalDataSource.observeAudit(id)
    }

    override fun observeAuditsFromUser(user_id: String): LiveData<Response<List<Audit>>> {
        return kbIdPassLocalDataSource.observeAuditsFromUser(user_id)
    }

    override suspend fun getAudits(): Response<List<Audit>> {
        return kbIdPassLocalDataSource.getAudits()
    }

    override suspend fun getAudit(id: Int): Response<Audit> {
        return kbIdPassLocalDataSource.getAudit(id)
    }

    override suspend fun getAuditsFromUser(user_id: String): Response<List<Audit>> {
        return kbIdPassLocalDataSource.getAuditsFromUser(user_id)
    }

    override suspend fun addAudit(audit: Audit) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.addAudit(audit) }
        }
    }
}