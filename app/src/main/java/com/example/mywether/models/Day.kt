package com.example.mywether.models

data class Day(
    val maxtemp_c: Float,
    val mintemp_c: Float,
    val avgtemp_c: Float,
    val condition: Condition,
)