package com.example.perfumeshop

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.perfumeshop.data.work.LoadWorker
import com.example.perfumeshop.data.work.SaveWorker
import com.example.perfumeshop.presentation.app.ui.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            workManager = WorkManager.getInstance(this)

            App(
                finishAffinity = {
                    finishAffinity()
                },
                loadData = {
                    val loadWorker = OneTimeWorkRequestBuilder<LoadWorker>().build()
                    workManager.enqueue(loadWorker)
                }
            )
        }
    }

    override fun onPause() {
        val saveWorker = OneTimeWorkRequestBuilder<SaveWorker>().build()
        workManager.enqueue(saveWorker)
        Log.d("LIFEC_TAG", "onStop: done in onPause")
        super.onPause()

    }

    override fun onStop() {
//        workManager.enqueue(saveWorker.build())
        Log.d("LIFEC_TAG", "onStop: done in onStop")

        super.onStop()
    }

    override fun onDestroy() {
//        workManager.enqueue(saveWorker.build())
//        Log.d("WORK_TAG", "onDestroy: done in onDestroy")
        Log.d("LIFEC_TAG", "onStop: done in onDest")
//        val saveWorker = OneTimeWorkRequestBuilder<SaveWorker>().build()
//        workManager.enqueue(saveWorker)
        super.onDestroy()
    }



//    override fun onStart() {
//        workManager.enqueue(saveWorker.build())
//
//        super.onStart()
//    }

}

