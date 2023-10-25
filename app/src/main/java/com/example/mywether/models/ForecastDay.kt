package com.example.mywether.models

data class ForecastDay(
    val date: String,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>,
)