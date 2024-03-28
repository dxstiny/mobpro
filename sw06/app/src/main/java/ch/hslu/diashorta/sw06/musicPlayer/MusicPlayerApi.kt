package ch.hslu.diashorta.sw06.musicPlayer

interface MusicPlayerApi {
    fun playNext(): String
    fun queryHistory(): List<String>
}