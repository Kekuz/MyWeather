package com.example.mywether

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v1/current.json") //https://api.weatherapi.com/v1/current.json?key=bdfd66d7ccc64e92881172652230509&q=Rostov-on-Don&aqi=no
    suspend fun getWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String,
    ): Weather
}