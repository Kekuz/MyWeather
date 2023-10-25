package com.example.mywether

import com.example.mywether.models.Condition
import com.example.mywether.models.Day
import com.example.mywether.models.ForecastDay

object LoadWeatherAdapter {
    private val forecastDay = ForecastDay(
        "-",
        Day(
            0.0f,
            0.0f,
            0.0f,
            Condition("-", "", 0)
        )
    )
    val weatherAdapter = WeatherAdapter(
        listOf(
            forecastDay,
            forecastDay,
            forecastDay,
            forecastDay,
        )
    )
}