package com.kbds.kbidpassreader.data.source

import androidx.lifecycle.LiveData
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import kotlinx.coroutines.*

class KBIdPassDefaultRepository(
    private val kbIdPassLocalDataSource: KBIdPassDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : KBIdPassRepository {

    override fun observeUsers(): LiveData<Response<List<UserEntity>>> {
        return kbIdPassLocalDataSource.observeUsers()
    }

    override fun observeUser(id: String): LiveData<Response<UserEntity>> {
        return kbIdPassLocalDataSource.observeUser(id)
    }

    override suspend fun getUsers(): Response<List<UserEntity>> {
        return kbIdPassLocalDataSource.getUsers()
    }

    override suspend fun getUser(id: String): Response<UserEntity> {
        return kbIdPassLocalDataSource.getUser(id)
    }

    override suspend fun addUser(user: UserEntity) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.addUser(user) }
        }
    }

    override suspend fun deleteUser(id: String) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.deleteUser(id) }
        }
    }

    override suspend fun updateUser(user: UserEntity) {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { kbIdPassLocalDataSource.updateUser(user) }
            }
        }
    }

    override suspend fun registerUser(user: UserEntity) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.registerUser(user) }
        }
    }

    override suspend fun deRegisterUser(user: UserEntity) {
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

    override fun observeLogs(): LiveData<Response<List<LogEntity>>> {
        return kbIdPassLocalDataSource.observeLogs()
    }

    override fun observeLog(id: Int): LiveData<Response<LogEntity>> {
        return kbIdPassLocalDataSource.observeLog(id)
    }

    override fun observeLogsFromUser(user_id: String): LiveData<Response<List<LogEntity>>> {
        return kbIdPassLocalDataSource.observeLogsFromUser(user_id)
    }

    override suspend fun getLogs(): Response<List<LogEntity>> {
        return kbIdPassLocalDataSource.getLogs()
    }

    override suspend fun getLog(id: Int): Response<LogEntity> {
        return kbIdPassLocalDataSource.getLog(id)
    }

    override suspend fun getLogsFromUser(user_id: String): Response<List<LogEntity>> {
        return kbIdPassLocalDataSource.getLogsFromUser(user_id)
    }

    override suspend fun addLog(log: LogEntity) {
        coroutineScope {
            launch { kbIdPassLocalDataSource.addLog(log) }
        }
    }

    override suspend fun refreshLogs() {
        kbIdPassLocalDataSource.refreshLogs()
    }
}