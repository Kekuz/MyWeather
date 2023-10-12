package com.example.mywether

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    const val apiKey = "bdfd66d7ccc64e92881172652230509"
    private const val weatherBaseUrl = "https://api.weatherapi.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(weatherBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService: WeatherAPI = retrofit.create(WeatherAPI::class.java)
}