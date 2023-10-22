package com.example.mywether

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.LocaleManagerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mywether.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
        //binding.recyclerView.adapter = WeatherAdapter(listOf())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        viewModel.enabledPermissionLive.observe(this) {
            requestPermission()
        }

        viewModel.resultLive.observe(this) {
            //binding.recyclerView.adapter = it
            binding.currentTemperature.text = "${it.current.temp_c.roundToInt()}°C"
            Glide.with(this)
                .load("https:${it.current.condition.icon.replace("64","128")}")
                //.placeholder(R.drawable.big_trackplaceholder)
                //.centerCrop()
                .into(binding.currentIcon)
            binding.loadPb.isVisible = false

            viewModel.cityLive.observe(this){ city ->
                binding.currentCity.text = city
            }
        }



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
