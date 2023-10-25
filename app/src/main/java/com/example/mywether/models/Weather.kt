package com.example.mywether.models

data class Weather(
    val location: Location,
    val current: Current,
    val forecast: Forecast,
)
