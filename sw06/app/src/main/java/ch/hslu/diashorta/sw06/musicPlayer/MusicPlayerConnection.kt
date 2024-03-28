package ch.hslu.diashorta.sw06.musicPlayer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class MusicPlayerConnection: ServiceConnection {
    private var musicPlayerApi: MusicPlayerApi? = null

    fun playNext(): String {
        return musicPlayerApi?.playNext() ?: "Service not connected"
    }

    fun queryHistory(): List<String> {
        return musicPlayerApi?.queryHistory() ?: emptyList()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicPlayerApi = null
    }
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        musicPlayerApi = service as MusicPlayerApi
    }

}