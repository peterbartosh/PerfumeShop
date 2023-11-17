package com.example.perfumeshop.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.perfumeshop.data.skeleton.DataManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LoadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataManager: DataManager
) : CoroutineWorker(context, workerParams){

    override suspend fun doWork(): Result {
        dataManager.loadProductsFromRemoteDatabase().onJoin
        return Result.success()
    }

}