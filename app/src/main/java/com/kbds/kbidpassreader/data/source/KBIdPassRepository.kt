package com.kbds.kbidpassreader.data.source

import com.kbds.kbidpassreader.logs.domain.model.Logs
import com.kbds.kbidpassreader.users.domain.model.User

class KBIdPassRepository(
    val kbIdPassLocalDataSource: KBIdPassDataSource
) : KBIdPassDataSource {

    override fun getUsers(callback: KBIdPassDataSource.LoadUsersCallback) {
        kbIdPassLocalDataSource.getUsers(object : KBIdPassDataSource.LoadUsersCallback {
            override fun onUsersLoaded(users: List<User>) {
                callback.onUsersLoaded(users)
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getUser(id: String, callback: KBIdPassDataSource.GetUserCallback) {
        kbIdPassLocalDataSource.getUser(id, object : KBIdPassDataSource.GetUserCallback {
            override fun onUserLoaded(user: User) {
                callback.onUserLoaded(user)
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }
        })
    }

    override fun addUser(user: User) {
        kbIdPassLocalDataSource.addUser(user)
    }

    override fun deleteUser(user: User) {
        kbIdPassLocalDataSource.deleteUser(user)
    }

    override fun updateUser(user: User) {
        kbIdPassLocalDataSource.updateUser(user)
    }

    override fun getLogs(callback: KBIdPassDataSource.LoadLogsCallback) {
        kbIdPassLocalDataSource.getLogs(object : KBIdPassDataSource.LoadLogsCallback {
            override fun onLogsLoaded(logs: List<Logs>) {
                callback.onLogsLoaded(logs)
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getLog(callback: KBIdPassDataSource.GetLogsCallback) {
        kbIdPassLocalDataSource.getLog(object : KBIdPassDataSource.GetLogsCallback {
            override fun onLogLoaded(log: Logs) {
                callback.onLogLoaded(log)
            }

            override fun onDataNotAvailable() {
                TODO("Not yet implemented")
            }
        })
    }

    override fun addLog(logs: Logs) {
        kbIdPassLocalDataSource.addLog(logs)
    }


    /*


    override fun saveTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            tasksRemoteDataSource.saveTask(it)
            tasksLocalDataSource.saveTask(it)
        }
    }

    override fun completeTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = true
            tasksRemoteDataSource.completeTask(it)
            tasksLocalDataSource.completeTask(it)
        }
    }

    override fun completeTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            completeTask(it)
        }
    }

    override fun activateTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = false
            tasksRemoteDataSource.activateTask(it)
            tasksLocalDataSource.activateTask(it)
        }
    }

    override fun activateTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            activateTask(it)
        }
    }

    override fun clearCompletedTasks() {
        tasksRemoteDataSource.clearCompletedTasks()
        tasksLocalDataSource.clearCompletedTasks()

        cachedTasks = cachedTasks.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }

    /**
     * Gets tasks from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [GetTaskCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val taskInCache = getTaskWithId(taskId)

        // Respond immediately with cache if available
        if (taskInCache != null) {
            callback.onTaskLoaded(taskInCache)
            return
        }

        EspressoIdlingResource.increment() // Set app as busy.

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        tasksLocalDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(task) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                    callback.onTaskLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                tasksRemoteDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
                    override fun onTaskLoaded(task: Task) {
                        // Do in memory cache update to keep the app UI up to date
                        cacheAndPerform(task) {
                            EspressoIdlingResource.decrement() // Set app as idle.
                            callback.onTaskLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        EspressoIdlingResource.decrement() // Set app as idle.
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    override fun refreshTasks() {
        cacheIsDirty = true
    }

    override fun deleteAllTasks() {
        tasksRemoteDataSource.deleteAllTasks()
        tasksLocalDataSource.deleteAllTasks()
        cachedTasks.clear()
    }

    override fun deleteTask(taskId: String) {
        tasksRemoteDataSource.deleteTask(taskId)
        tasksLocalDataSource.deleteTask(taskId)
        cachedTasks.remove(taskId)
    }

    private fun getTasksFromRemoteDataSource(callback: TasksDataSource.LoadTasksCallback) {
        tasksRemoteDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                refreshLocalDataSource(tasks)

                EspressoIdlingResource.decrement() // Set app as idle.
                callback.onTasksLoaded(ArrayList(cachedTasks.values))
            }

            override fun onDataNotAvailable() {
                EspressoIdlingResource.decrement() // Set app as idle.
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(tasks: List<Task>) {
        cachedTasks.clear()
        tasks.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(tasks: List<Task>) {
        tasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            tasksLocalDataSource.saveTask(task)
        }
    }

    private fun getTaskWithId(id: String) = cachedTasks[id]

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask = Task(task.title, task.description, task.id).apply {
            isCompleted = task.isCompleted
        }
        cachedTasks.put(cachedTask.id, cachedTask)
        perform(cachedTask)
    }

    companion object {

        private var INSTANCE: TasksRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param tasksRemoteDataSource the backend data source
         * *
         * @param tasksLocalDataSource  the device storage data source
         * *
         * @return the [TasksRepository] instance
         */
        @JvmStatic fun getInstance(tasksRemoteDataSource: TasksDataSource,
                tasksLocalDataSource: TasksDataSource) =
                INSTANCE ?: synchronized(TasksRepository::class.java) {
                    INSTANCE ?: TasksRepository(tasksRemoteDataSource, tasksLocalDataSource)
                            .also { INSTANCE = it }
                }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
     */
}