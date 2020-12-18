package com.kbds.kbidpassreader.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface BaseCoroutineScope : CoroutineScope{
    val job: Job
    fun releaseCoroutine()
}