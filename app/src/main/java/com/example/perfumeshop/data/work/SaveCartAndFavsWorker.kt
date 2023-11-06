package com.example.perfumeshop.data.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@HiltWorker
class SaveCartAndFavsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository
) : CoroutineWorker(context, workerParams){

    override suspend fun doWork(): Result {
        Log.d("WORK_TAG", "onStop: done")
        val saver = Saver(roomRepository, fireRepository)
        CoroutineScope(Job()).launch {
            saver.saveCartAndFavs()
        }.onJoin
        Log.d("WORK_TAG", "onStop: done")
        return Result.success()
    }

}