package com.example.perfumeshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.perfumeshop.data.work.SaveCartAndFavsWorker
import com.example.perfumeshop.presentation.app.ui.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            workManager = WorkManager.getInstance(this)

            App(finishAffinity = { finishAffinity() })
        }
    }

    override fun onPause() {

        val saveWorker = OneTimeWorkRequestBuilder<SaveCartAndFavsWorker>().build()
        workManager.enqueue(saveWorker)

        super.onPause()

    }

    override fun onStop() {
//        workManager.enqueue(saveWorker.build())
//        Log.d("WORK_TAG", "onStop: done in onStop")

        super.onStop()
    }

    override fun onDestroy() {
//        workManager.enqueue(saveWorker.build())
//        Log.d("WORK_TAG", "onDestroy: done in onDestroy")
        super.onDestroy()
    }



//    override fun onStart() {
//        workManager.enqueue(saveWorker.build())
//
//        super.onStart()
//    }

}

