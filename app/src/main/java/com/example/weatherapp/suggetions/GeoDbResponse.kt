package com.example.weatherapp.suggetions

data class GeoDbResponse(
    val data: List<GeoCity>
)

data class GeoCity(
    val city: String
)
