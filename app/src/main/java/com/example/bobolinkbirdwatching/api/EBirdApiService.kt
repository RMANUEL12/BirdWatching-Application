package com.example.bobolinkbirdwatching.api

import com.example.bobolinkbirdwatching.BuildConfig
import com.example.bobolinkbirdwatching.data.Observation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EBirdApiService {

    // Fetch recent observations for a specific region
    @GET("data/obs/region/recent")
    fun getRecentObservationsForRegion(
        @Query("r") region: String = "ZA",
        @Query("key") apiKey: String = BuildConfig.EBIRD_API_KEY
    ): Call<List<Observation>>

}
