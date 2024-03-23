package ch.hslu.diashorta.sw05

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.HttpURLConnection


class BandsViewModel: ViewModel() {
    private val _bandsFlow: MutableStateFlow<List<BandCode>> =
        MutableStateFlow(emptyList())
    val bandsFlow: Flow<List<BandCode>> = _bandsFlow
    val bands: List<BandCode> get() = _bandsFlow.value

    private val _currentBand: MutableSharedFlow<BandInfo?> = MutableSharedFlow()
    val currentBand: Flow<BandInfo?> = _currentBand

    private val api = Retrofit.Builder()
        .client(OkHttpClient.Builder().build())
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .addConverterFactory(Json.asConverterFactory(
            "application/json; charset=UTF8".toMediaType()
        ))
        .build()
    private val bandsService = api.create(BandsApiService::class.java)

    init {
        _bandsFlow.value = emptyList()
    }

    fun reset() {
        viewModelScope.launch {
            _bandsFlow.emit(emptyList())
            _currentBand.emit(null)
        }
    }

    fun requestBands() {
        viewModelScope.launch {
            val bands = getBandCodesFromServer()
            bands?.let {
                _bandsFlow.emit(bands)
            }
        }
    }

    fun requestBandInfo(bandCode: String) {
        viewModelScope.launch {
            val bandInfo = getBandInfoFromServer(bandCode)
            _currentBand.emit(bandInfo)
        }
    }

    private suspend fun getBandCodesFromServer(): List<BandCode>? {
        val response = bandsService.getBandNames()
        if (response.code() == HttpURLConnection.HTTP_OK) {
            return response.body().orEmpty()
        }
        return null
    }

    private suspend fun getBandInfoFromServer(bandCode: String): BandInfo? {
        val response = bandsService.getBandInfo(bandCode)
        if (response.code() == HttpURLConnection.HTTP_OK) {
            return response.body()
        }
        return null
    }
}