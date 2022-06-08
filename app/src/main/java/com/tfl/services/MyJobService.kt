package com.tfl.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService: JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartJob")
        coroutineScope.launch {
            var workItem = p0?.dequeueWork()
            while (workItem != null) {
                val page = workItem.intent?.getIntExtra(PAGE, 0) ?: 0
                for (i in 0 until 5) {
                    delay(1000)
                    log("Timer $i $page")
                }
                p0?.completeWork(workItem)
                workItem = p0?.dequeueWork()
            }
            jobFinished(p0, false)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    companion object {
        const val JOB_ID = 1
        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }

}