package com.example.mywether


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel : ViewModel() {

    val resultLive = MutableLiveData<WeatherAdapter>()

    fun getWeatherInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val currentWeatherResponse = RetrofitBuilder.weatherService.getWeather(
                    RetrofitBuilder.apiKey,
                    "Rostov-on-Don",
                    "3",
                    "no",
                    "no",
                )
                if (currentWeatherResponse.isSuccessful){
                    Log.e("MyLog", currentWeatherResponse.body()?.forecast?.forecastday.toString())
                    resultLive.postValue(WeatherAdapter(currentWeatherResponse.body()?.forecast?.forecastday!!))
                }
            }catch (e: Exception){
                Log.e("MyLog", e.message.toString())
            }

        }
    }


}