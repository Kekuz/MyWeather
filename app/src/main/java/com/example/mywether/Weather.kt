package com.example.mywether

data class Condition(
    val text: String,
)

data class Current(
    val last_updated: String,
    val temp_c: Float,
    val condition: Condition,
)

data class Forecast(
    val forecastday: List<ForecastDay>,
)
data class ForecastDay(
    val date: String,
    val day: Day,
)
data class Day(
    val avgtemp_c: Float,
    val uv: Float,
    val condition: Condition,
)

data class Weather(
    val current: Current,
    val forecast: Forecast,
)