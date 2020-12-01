package com.kbds.kbidpassreader.xxxtestxxx

import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kbds.kbidpassreader.base.BaseCoroutineScope
import com.kbds.kbidpassreader.util.UICoroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor

class UICoroutineScopeSampleActivity(
    scope: BaseCoroutineScope = UICoroutineScope()
) : AppCompatActivity(), BaseCoroutineScope by scope {

    fun loadUI() = launch {
        val ioData = async(Dispatchers.IO) {
            // io Job
        }
        val data = ioData.await() // wait IO Job

        //draw(data) UI Thread
    }

    fun oneClickButton(button: Button) {
        button.onClick {
            for (i in 10 downTo 1) {
                button.text = "$i"
                delay(1000)
            }
        }
    }

    fun multiJob() {
        launch(coroutineContext) {
            launch(coroutineContext) {  }
        }

        val job = Job()

        launch(job) {
            launch(job) {

            }
        }
    }

    fun View.onClick(action: suspend (View) -> Unit) {
        val event = GlobalScope.actor<View>(Dispatchers.Main) {
            for (event in channel) action(event)
        }

        setOnClickListener {
            event.offer(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCoroutine()
    }
}