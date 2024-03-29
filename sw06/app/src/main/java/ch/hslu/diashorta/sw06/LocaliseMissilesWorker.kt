package ch.hslu.diashorta.sw06

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class LocaliseMissilesWorker(Context: Context, WorkerParameters: WorkerParameters)
    : Worker(Context, WorkerParameters) {
    override fun doWork(): Result {
        Log.d("LocaliseMissilesWorker", "Localising missiles...")
        val output: Data = workDataOf(
            "missilePositions" to arrayOf("71.235 N, 044.863 E", "72.235 N, 044.863 E", "73.235 N, 044.863 E"))
        Thread.sleep(5000)
        return Result.success(output)
    }
}