package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class NominatimResult(
    @param:Json(name = "place_id") val placeId: Long = 0,
    @param:Json(name = "lat") val lat: String = "0.0",
    @param:Json(name = "lon") val lon: String = "0.0",
    @param:Json(name = "display_name") val displayName: String = "",
    @param:Json(name = "type") val type: String? = null,
    @param:Json(name = "class") val placeClass: String? = null
)

interface NominatimApi {
    @GET("search")
    suspend fun searchLocations(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("addressdetails") addressDetails: Int = 1,
        @Query("limit") limit: Int = 5,
        @Query("countrycodes") countryCodes: String = "in",
        @Header("User-Agent") userAgent: String = "HiddenIndian-AndroidApp/1.0 (contact: aakashsinghsingh276@gmail.com)"
    ): List<NominatimResult>
}

object NetworkClient {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    val nominatimApi: NominatimApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NominatimApi::class.java)
    }
}
