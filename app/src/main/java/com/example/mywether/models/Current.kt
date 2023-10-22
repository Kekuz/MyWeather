package com.example.mywether.models

data class Current(
    val temp_c: Float,
    val is_day: Int,
    val condition: Condition,
    val wind_kph: Float,
    val wind_degree: Int,
    val precip_mm: Float,
    val humidity: Int,
    val cloud: Int,


)
