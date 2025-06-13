package com.example.weatherapp.suggetions

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoDbApi {
    @GET("v1/geo/cities")
    suspend fun getCitySuggestions(
        @Query("namePrefix") prefix: String,
        @Query("limit") limit: Int = 5
    ): Response<GeoDbResponse>
}
