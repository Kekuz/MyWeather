package com.example.mywether.models

data class Hour(
    val time: String,
    val temp_c: Float,
    val condition: Condition,
    val wind_kph: Float,
    val wind_degree: Int,
    val pressure_in: Float,
    val precip_mm: Float,
    val humidity: Int,
    val cloud: Int,
    val feelslike_c: Float,
    val chance_of_rain : Int,
    val chance_of_snow: Int,
    val vis_km: Int,

)