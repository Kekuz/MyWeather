package com.example.mywether

data class Condition(
    val text: String,
)

data class Current(
    val last_updated_epoch: Long,
    val temp_c: Float,
    val condition: Condition,
)

data class Weather(
    val current: Current
)