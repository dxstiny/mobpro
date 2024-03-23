package ch.hslu.diashorta.sw05

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

@Serializable
data class BandCode(
    val name: String,
    val code: String
)

@Serializable
data class BandInfo(
    val name: String,
    val foundingYear: Int,
    val homeCountry: String,
    val bestOfCdCoverImageUrl: String?
)

interface BandsApiService {
    @GET("all.json")
    suspend fun getBandNames(): Response<List<BandCode>>

    @GET("info/{code}.json")
    suspend fun getBandInfo(@Path("code") code: String): Response<BandInfo>
}