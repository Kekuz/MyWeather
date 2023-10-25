package com.example.mywether

import com.example.mywether.models.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v1/forecast.json") //https://api.weatherapi.com/v1/forecast.json?key=bdfd66d7ccc64e92881172652230509&q=Rostov-on-Don&days=3&aqi=no&alerts=no
    suspend fun getWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: String,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String,
        @Query("lang") lang: String,
    ): Response<Weather>
}