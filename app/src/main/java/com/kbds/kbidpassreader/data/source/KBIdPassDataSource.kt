package com.kbds.kbidpassreader.data.source

import androidx.annotation.NonNull
import com.kbds.kbidpassreader.logs.domain.model.Logs
import com.kbds.kbidpassreader.users.domain.model.User

interface KBIdPassDataSource {

    interface LoadUsersCallback {
        fun onUsersLoaded(users: List<User>)
        fun onDataNotAvailable()
    }

    interface GetUserCallback {
        fun onUserLoaded(user: User)
        fun onDataNotAvailable()
    }

    fun getUsers(@NonNull callback: LoadUsersCallback)

    fun getUser(@NonNull id: String, @NonNull callback: GetUserCallback)

    fun addUser(@NonNull user: User)

    fun deleteUser(@NonNull user: User)

    fun updateUser(@NonNull user: User)


    interface LoadLogsCallback {
        fun onLogsLoaded(logs: List<Logs>)
        fun onDataNotAvailable()
    }

    interface GetLogsCallback {
        fun onLogLoaded(log: Logs)
        fun onDataNotAvailable()
    }

    fun getLogs(@NonNull callback: LoadLogsCallback)

    fun getLog(@NonNull callback: GetLogsCallback)

    fun addLog(@NonNull logs: Logs)

    /*
    interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void saveTask(@NonNull Task task);

    void completeTask(@NonNull Task task);

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Task task);

    void activateTask(@NonNull String taskId);

    void clearCompletedTasks();

    void refreshTasks();

    void deleteAllTasks();

    void deleteTask(@NonNull String taskId);
     */
}