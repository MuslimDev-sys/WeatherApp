package com.example.weatherapp.suggetions

import com.example.weatherapp.suggetions.CityApiClient.api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun fetchCitySuggestions(query: String, onResult: (List<String>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = api.getCitySuggestions(query)
            if (response.isSuccessful) {
                val cities = response.body()?.data?.map { it.city } ?: emptyList()
                withContext(Dispatchers.Main) {
                    onResult(cities)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onResult(emptyList())
            }
        }
    }
}
