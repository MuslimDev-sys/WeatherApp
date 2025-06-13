package com.example.weatherapp.suggetions

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CityApiClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key", "99e499f8f5msha21b54ce9ac4925p1ecb34jsnd6c6eaf0939d")
                .addHeader("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://wft-geo-db.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: GeoDbApi = retrofit.create(GeoDbApi::class.java)
}
