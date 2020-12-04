package com.kbds.kbidpassreader.xxxtestxxx

import kotlinx.coroutines.*

val scope1 = CoroutineScope(Job() + Dispatchers.Default)

fun main() = runBlocking {
    scope1.launch {
        launch {
            repeat(100) {
                test1()
            }
        }
        launch {
            repeat(100) {
                test2()
            }
        }
    }.join()
}

suspend fun test1() {
    delay(100)
    println(1)
}

suspend fun test2() {
    delay(100)
    println(2)
}