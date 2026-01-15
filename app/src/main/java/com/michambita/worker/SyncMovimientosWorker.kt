package com.michambita.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.michambita.domain.usecase.SyncMovimientosUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class SyncMovimientosWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncMovimientosUseCase: SyncMovimientosUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        // Verificar que sea después de medianoche
        if (!isAfterMidnight()) {
            return Result.retry()
        }

        return try {
            val result = syncMovimientosUseCase()
            
            if (result.isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun isAfterMidnight(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        // Ejecutar entre 12 AM y 6 AM
        return hour in 0..5
    }
}
