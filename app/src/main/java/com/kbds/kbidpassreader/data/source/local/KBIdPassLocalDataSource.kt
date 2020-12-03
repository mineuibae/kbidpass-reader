package com.kbds.kbidpassreader.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kbds.kbidpassreader.data.Response
import com.kbds.kbidpassreader.data.Response.Error
import com.kbds.kbidpassreader.data.Response.Success
import com.kbds.kbidpassreader.data.source.KBIdPassDataSource
import com.kbds.kbidpassreader.data.source.local.dao.AuditDao
import com.kbds.kbidpassreader.data.source.local.dao.UserDao
import com.kbds.kbidpassreader.domain.model.audit.Audit
import com.kbds.kbidpassreader.domain.model.user.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KBIdPassLocalDataSource internal constructor(
    private val userDao: UserDao,
    private val auditDao: AuditDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): KBIdPassDataSource {

    override fun observeUsers(): LiveData<Response<List<User>>> =
        userDao.observeUsers().map {
            Success(it)
        }

    override fun observeUser(id: String): LiveData<Response<User>> =
        userDao.observeUser(id).map {
            Success(it)
        }

    override suspend fun getUsers(): Response<List<User>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(userDao.getUsers())
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun getUser(id: String): Response<User> =
        withContext(ioDispatcher) {
            try {
                val user = userDao.getUser(id)
                if(user != null) {
                    return@withContext Success(user)
                } else {
                    return@withContext Error(Exception("User not found"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun addUser(user: User) =
        withContext(ioDispatcher) {
            userDao.addUser(user)
        }

    override suspend fun deleteUser(user: User) =
        withContext(ioDispatcher) {
            userDao.deleteUser(user.id)
        }

    override suspend fun updateUser(user: User) =
        withContext(ioDispatcher) {
            userDao.updateUser(user)
        }

    override suspend fun registerUser(user: User) =
        withContext(ioDispatcher) {
            userDao.updateRegistered(user.id, true)
        }

    override suspend fun deRegisterUser(user: User) =
        withContext(ioDispatcher) {
            userDao.updateRegistered(user.id, false)
        }

    override suspend fun refreshUsers() {
        // NO-OP
    }

    override suspend fun refreshUser(id: String) {
        // NO-OP
    }


    override fun observeAudits(): LiveData<Response<List<Audit>>> =
        auditDao.observeAudits().map {
            Success(it)
        }

    override fun observeAudit(id: Int): LiveData<Response<Audit>> =
        auditDao.observeAudit(id).map {
            Success(it)
        }

    override fun observeAuditsFromUser(user_id: String): LiveData<Response<List<Audit>>> =
        auditDao.observeAuditsFromUser(user_id).map {
            Success(it)
        }

    override suspend fun getAudits(): Response<List<Audit>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(auditDao.getAudits())
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun getAudit(id: Int): Response<Audit> =
        withContext(ioDispatcher) {
            try {
                val audit = auditDao.getAudit(id)
                if(audit != null) {
                    return@withContext Success(audit)
                } else {
                    return@withContext Error(Exception("Audit not found"))
                }
            } catch (e: Exception) {
                return@withContext Error(e)
            }
        }

    override suspend fun getAuditsFromUser(user_id: String): Response<List<Audit>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Success(auditDao.getAuditsFromUser(user_id))
            } catch (e: Exception) {
                Error(e)
            }
        }

    override suspend fun addAudit(audit: Audit) =
        withContext(ioDispatcher) {
            auditDao.addAudit(audit)
        }

    override suspend fun refreshAudits() {
        // NO-OP
    }
}