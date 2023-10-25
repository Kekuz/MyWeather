package com.example.mywether

import com.example.mywether.models.Astro
import com.example.mywether.models.Condition
import com.example.mywether.models.Day
import com.example.mywether.models.ForecastDay
import com.example.mywether.models.Hour

object LoadWeatherAdapter {
    private val forecastDay = ForecastDay(
        "-",
        Day(
            0.0f,
            0.0f,
            0.0f,
            Condition("-", "", 0)
        ),
        Astro("", "", "", "", "", "", 0, 0),
        listOf(Hour("", 0.0f, Condition("-", "", 0), 0.0f, 0, 0.0f, 0.0f, 0, 0, 0.0f, 0, 0, 0))
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