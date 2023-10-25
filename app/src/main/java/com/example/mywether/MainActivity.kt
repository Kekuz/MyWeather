package com.example.mywether

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.LocaleManagerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mywether.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.time.LocalDate
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.forecastRv.adapter = LoadWeatherAdapter.weatherAdapter


        binding.poweredByLinkTv.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.powered_link))
                startActivity(this)
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        viewModel.enabledPermissionLive.observe(this) {
            requestPermission()
        }

        viewModel.resultLive.observe(this) {
            with(binding) {
                forecastRv.adapter = WeatherAdapter(it.forecast.forecastday)

                weatherCl.setBackgroundColor(getColor(weatherConditionColor(it.current.condition.code, it.current.is_day)))

                if (it.current.is_day == 1) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                currentTemperature.text = "${it.current.temp_c.roundToInt()}°C"
                Glide.with(this@MainActivity)
                    .load("https:${it.current.condition.icon.replace("64", "128")}")
                    .into(currentIcon)

                viewModel.cityLive.observe(this@MainActivity) { city ->
                    currentCity.text = city
                }

                val windSpeedMs = (it.current.wind_kph / 3.6).roundToInt()

                windSpeedFl.backgroundTintList = getColorStateList(windBackgroundColor(windSpeedMs))
                if (it.current.is_day == 0) {
                    val textColor = getColor(R.color.black)
                    windSpeedInfo.setTextColor(textColor)
                    windKphTv.setTextColor(textColor)
                    windArrowIv.imageTintList = getColorStateList(R.color.black)
                }

                windKphTv.text = "$windSpeedMs м/с"
                windArrowIv.animate().rotation(it.current.wind_degree.toFloat()) //тут анмация
                //windArrowIv.rotation = it.current.wind_degree.toFloat() //тут анимации нет

                val precip = it.current.precip_mm.toInt()
                precipMmTv.text = "$precip мм"
                waterDropIv.setImageResource(rainDropsCount(precip))

                humidityPercentTv.text = "${it.current.humidity}%"

                cloudPercentTv.text = "${it.current.cloud}%"

                loadPb.isVisible = false

                var code = viewModel.translate.map[it.current.condition.code]

                //Костыль для выбора ясно/солнечно
                if (it.current.condition.code == 1000) {
                    code += it.current.is_day
                }

                currentFl.setOnClickListener { _ ->
                    Toast.makeText(this@MainActivity, "$code", Toast.LENGTH_SHORT).show()
                }
            }


        }


    }

    private fun windBackgroundColor(speed: Int): Int =
        when (speed) {
            0 -> R.color.calm
            in 1..5 -> R.color.weak
            in 6..7 -> R.color.moderate
            in 8..15 -> R.color.strong
            in 16..21 -> R.color.storm
            in 22..100 -> R.color.hurricane
            else -> R.color.white
        }

    private fun weatherConditionColor(code: Int, isDay: Int): Int =
        when (code) {
            1000 -> if (isDay == 1) R.color.day else R.color.night //солнце или ясно
            1066, 1069, 1072, 1114, 1117, 1150, 1153, 1168, 1171, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258, 1279, 1282 -> if (isDay == 1) R.color.snow_day else R.color.snow_night //снег
            1003, 1006, 1009 -> if (isDay == 1) R.color.cloudy_day else R.color.cloudy_night //облачно
            1030, 1135, 1147 -> if (isDay == 1) R.color.fog_day else R.color.fog_night //туман
            1063, 1087, 1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201, 1204, 1207, 1237, 1240, 1243, 1246, 1249, 1252, 1261, 1264, 1273, 1276 -> if (isDay == 1) R.color.rain_day else R.color.rain_night //дождь
            else -> R.color.white
        }

    private fun rainDropsCount(precip: Int): Int =
        when (precip) {
            0 -> R.drawable.rain_icon_0
            in 1..5 -> R.drawable.rain_icon_1
            in 6..15 -> R.drawable.rain_icon_2
            else -> R.drawable.rain_icon_3
        }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getLastKnownLocation()
            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_LONG).show()
                binding.loadPb.isVisible = false
            }
        }
    }

}
