package com.kbds.kbidpassreader.data

sealed class Response<out R> {
    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Error(
        val exception: Exception,
        val data: Any? = null
    ): Response<Nothing>()

    object Loading : Response<Nothing>()
}

val Response<*>.succeeded
    get() = this is Response.Success && data != null