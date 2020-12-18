package com.kbds.kbidpassreader.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.Response.Error
import com.kbds.kbidpassreader.data.Response.Success
import com.kbds.kbidpassreader.data.source.KBIdPassDataSource
import com.kbds.kbidpassreader.data.source.local.dao.LogDao
import com.kbds.kbidpassreader.data.source.local.dao.UserDao
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.user.UserEntity
import com.kbds.kbidpassreader.extension.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KBIdPassLocalDataSource internal constructor(
    private val userDao: UserDao,
    private val logDao: LogDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : KBIdPassDataSource {

    override fun observeUsers(): LiveData<Response<List<UserEntity>>> =
        userDao.observeUsers().map { userTables ->
            Success(
                userTables.map { userTable ->
                    userTable.map()
                }
            )
        }

    override fun observeUser(id: String): LiveData<Response<UserEntity>> =
        userDao.observeUser(id).map { userTable ->
            Success(userTable.map())
        }

    override suspend fun getUsers(): Response<List<UserEntity>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(
                    userDao.getUsers().map { userTable ->
                        userTable.map()
                    }
                )
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun getUser(id: String): Response<UserEntity> =
        withContext(ioDispatcher) {
            try {
                val user = userDao.getUser(id)?.map()
                if (user != null) {
                    return@withContext Success(user)
                } else {
                    return@withContext Error(Exception("User not found"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun addUser(user: UserEntity) =
        withContext(ioDispatcher) {
            userDao.addUser(user.map())
        }

    override suspend fun deleteUser(id: String) =
        withContext(ioDispatcher) {
            userDao.deleteUser(id)
        }

    override suspend fun updateUser(user: UserEntity) =
        withContext(ioDispatcher) {
            userDao.updateUser(user.map())
        }

    override suspend fun registerUser(user: UserEntity) =
        withContext(ioDispatcher) {
            userDao.updateRegistered(user.id, true)
        }

    override suspend fun deRegisterUser(user: UserEntity) =
        withContext(ioDispatcher) {
            userDao.updateRegistered(user.id, false)
        }

    override suspend fun refreshUsers() {
        // NO-OP
    }

    override suspend fun refreshUser(id: String) {
        // NO-OP
    }


    override fun observeLogs(): LiveData<Response<List<LogEntity>>> =
        logDao.observeLogs().map { logTables ->
            Success(
                logTables.map { logTable ->
                    logTable.map()
                }
            )
        }

    override fun observeLog(id: Int): LiveData<Response<LogEntity>> =
        logDao.observeLog(id).map { logTable ->
            Success(logTable.map())
        }

    override fun observeLogsFromUser(user_id: String): LiveData<Response<List<LogEntity>>> =
        logDao.observeLogsFromUser(user_id).map { logTables ->
            Success(
                logTables.map { logTable ->
                    logTable.map()
                }
            )
        }

    override suspend fun getLogs(): Response<List<LogEntity>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(
                    logDao.getLogs().map { logTable ->
                        logTable.map()
                    }
                )

            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun getLog(id: Int): Response<LogEntity> =
        withContext(ioDispatcher) {
            try {
                val log = logDao.getLog(id)?.map()
                if (log != null) {
                    return@withContext Success(log)
                } else {
                    return@withContext Error(Exception("Log not found"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun getLogsFromUser(user_id: String): Response<List<LogEntity>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(
                    logDao.getLogsFromUser(user_id).map { logTable ->
                        logTable.map()
                    }
                )
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun addLog(log: LogEntity) =
        withContext(ioDispatcher) {
            logDao.addLog(log.map())
        }

    override suspend fun refreshLogs() {
        // NO-OP
    }
}