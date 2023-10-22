package com.example.mywether.models

data class Day(
    val avgtemp_c: Float,
    val uv: Float,
    val condition: Condition,
)