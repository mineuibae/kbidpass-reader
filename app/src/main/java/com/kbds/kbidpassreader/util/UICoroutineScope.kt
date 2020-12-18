package com.kbds.kbidpassreader.util

import com.kbds.kbidpassreader.base.BaseCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class UICoroutineScope(private val dispatchers: CoroutineContext = Dispatchers.Main) : BaseCoroutineScope {
    override val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatchers + job

    override fun releaseCoroutine() {
        job.cancel()
    }
}