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
                    "no"
                )
                if (currentWeatherResponse.isSuccessful){
                    Log.d("MyLog", currentWeatherResponse.body().toString())
                    resultLive.postValue(WeatherAdapter(listOf(currentWeatherResponse.body()!!)))
                }
            }catch (e: Exception){
                Log.e("MyLog", e.message.toString())
            }

        }
    }


}