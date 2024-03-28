package ch.hslu.diashorta.sw06.musicPlayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import ch.hslu.diashorta.sw06.MainActivity
import ch.hslu.diashorta.sw06.R

class MusicPlayerService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private val PLAYLIST = arrayOf(
            "Diabarha - Uranoid",
            "Diabarha - Genocide",
            "Rick Astley - Never Gonna Give You Up",
            "Sunstroke Project - Run Away",
            "Toto - Africa",
            "Gregor Salto & Wiwek - Intimi",
            "Tony Igy - Astronomia",
            "Axel F",
            "Ryan Gosling - I'm Just Ken"
        )
    }

    inner class MusicPlayerApiImpl : Binder(), MusicPlayerApi {
        override fun playNext(): String {
            return this@MusicPlayerService.playNext()
        }

        override fun queryHistory(): List<String> {
            return this@MusicPlayerService.queryHistory()
        }
    }

    private val binder = MusicPlayerApiImpl()
    private val history = mutableListOf<String>()


    private fun queryHistory(): List<String> {
        return history
    }

    private fun playNext(): String {
        val next = PLAYLIST.random()
        history.add(next)
        startForeground(NOTIFICATION_ID, createNotification(next))
        return next
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        Log.i("MusicPlayerService", "Service created")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("MusicPlayerService", "Service started")
        startPlayer()
        return START_STICKY
    }

    private fun startPlayer() {
        Log.i("MusicPlayerService", "Player started")
        startForeground(NOTIFICATION_ID, createNotification("--waiting--"))
    }

    private fun createNotification(s: String): Notification {
        return NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setContentTitle("HSLU Music Player")
            .setTicker("HSLU Music Player")
            .setContentText(s)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setWhen(System.currentTimeMillis())
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun onDestroy() {
        Log.i("MusicPlayerService", "Service destroyed")
        stopForeground(true)
    }
}