package com.example.weatherapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun fetchWeather(city: String, onResult: (String) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(WeatherApi::class.java)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = service.getWeather(city, "d3e243aa074a7a27067c5b03f3871733")
            if (response.isSuccessful) {
                val data = response.body()
                val desc = data?.weather?.firstOrNull()?.description ?: "Unknown"
                val temp = data?.main?.temp
                val result = "${data?.name}: $desc, $temp°C"
                withContext(Dispatchers.Main) {
                    onResult(result)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult("City not found.")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onResult("Error: ${e.message}")
            }
        }
    }
}
