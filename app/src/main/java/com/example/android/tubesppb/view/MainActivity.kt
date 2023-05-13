@file:Suppress("DEPRECATION")

package com.example.android.tubesppb.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.tubesppb.R
import com.example.android.tubesppb.adapter.menu.Menu
import com.example.android.tubesppb.adapter.menu.MenuAdapters
import com.example.android.tubesppb.databinding.ActivityMainBinding
import com.example.android.tubesppb.networking.Api
import com.example.android.tubesppb.room.user.User
import com.example.android.tubesppb.room.user.UserDB
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db by lazy { UserDB(this) }
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var menuAdapter: MenuAdapters
    private lateinit var menu: Menu
    private var modelMenuList: MutableList<Menu> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        getUser().observe(this) { user ->
            // Update UI with user data
            if (user != null) {
                binding.toolbar.username.text = user.userName
                binding.history.text=user.userName + ", Periksa Riwayat Pesananmu"
            }
        }
        binding.toolbar.toolbar.setOnClickListener {
            getUser().observe(this) { user ->
                user?.let {
                    val profilIntent = Intent(this, Profil::class.java).apply {
                        putExtra("USER_DATA", user)
                    }
                    startActivity(profilIntent)
                }
            }
        }
        setwidth()
        setMenu()
        binding.layoutHistory.setOnClickListener {
            val history = Intent(this, History::class.java)
            startActivity(history)
        }
    }

    private fun setwidth() {
        val layoutHistory = binding.layoutHistory
        val linearLayout = binding.info1
        val weatherCardView: CardView = binding.weatherCardView
        val display: Display = windowManager.defaultDisplay
        val width = display.width

        if (width > 1000) {
            weatherCardView.layoutParams.width = 1000
            layoutHistory.layoutParams.width = 1000
            linearLayout.layoutParams.width = 1000
        } else {
            weatherCardView.layoutParams.width = width
            layoutHistory.layoutParams.width = width
            linearLayout.layoutParams.width = width
        }
    }


    private fun getUser(): LiveData<User?> {
        val uid = firebaseAuth.currentUser?.uid.toString()
        val userLiveData = MutableLiveData<User?>()
        uid.let {
            CoroutineScope(Dispatchers.IO).launch {
                val user = db.UserDao().getUser(uid)
                withContext(Dispatchers.Main) {
                    userLiveData.value = user
                }
            }
        }
        return userLiveData
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMenu() {
        menu = Menu(R.drawable.ic_cuci_basah, "Cuci Basah")
        modelMenuList.add(this.menu)
        menu = Menu(R.drawable.ic_dry_cleaning, "Dry Cleaning")
        modelMenuList.add(this.menu)
        menu = Menu(R.drawable.ic_premium_wash, "Premium Wash")
        modelMenuList.add(this.menu)
        menu = Menu(R.drawable.ic_setrika, "Setrika")
        modelMenuList.add(this.menu)

        menuRecyclerView = binding.rvMenu
        menuAdapter = MenuAdapters(this, modelMenuList)
        menuRecyclerView.adapter = menuAdapter

        menuAdapter.setOnItemClickListener(object : MenuAdapters.OnItemClickListener {
            override fun onItemClick(modelMenu: Menu) {
                val intent: Intent
                when (modelMenu.title) {
                    "Cuci Basah" -> {
                        intent = Intent(this@MainActivity, CuciBasah::class.java)
                        intent.putExtra(CuciBasah.DATA_TITLE, modelMenu.title)
                    }
                    "Dry Cleaning" -> {
                        intent = Intent(this@MainActivity, DryClean::class.java)
                        intent.putExtra(DryClean.DATA_TITLE, modelMenu.title)
                    }
                    "Premium Wash" -> {
                        intent = Intent(this@MainActivity, PremiumWash::class.java)
                        intent.putExtra(PremiumWash.DATA_TITLE, modelMenu.title)
                    }
                    "Setrika" -> {
                        intent = Intent(this@MainActivity, Setrika::class.java)
                        intent.putExtra(Setrika.DATA_TITLE, modelMenu.title)
                    }
                    else -> return
                }
                startActivity(intent)
                Log.d("MenuListener", "Menu item clicked: ${modelMenu.title}")
                Toast.makeText(
                    this@MainActivity,
                    "Menu item clicked: ${modelMenu.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 1000 // Interval waktu minimum dalam milidetik
            fastestInterval = 500 // Interval waktu tercepat dalam milidetik
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val apiManager = Api()
        val weatherService = apiManager.createWeatherService()
        locationCallback = object : LocationCallback() {
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Menggunakan data lokasi yang diperoleh di sini
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val apiKey = "6e3ff2ab6876594ddc2a3cd5e4521578"
                    val units = "metric"
                    val language = "id"

                    CoroutineScope(Dispatchers.Main).launch {
                        val response = weatherService.getWeatherData(
                            latitude,
                            longitude,
                            apiKey,
                            units,
                            language
                        )
                        if (response.isSuccessful) {
                            val weatherData = response.body()
                            val weatherMain = weatherData?.weather?.get(0)?.main
                            val weatherDescription = weatherData?.weather?.get(0)?.description
                            val temperature = weatherData?.main?.temp
                            val cityName = weatherData?.name
                            val icon = weatherData?.weather?.get(0)?.icon
                            if (icon != null) {
                                val iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"
                                val main : TextView = binding.main
                                val cityNameTextView: TextView = binding.cityNameTextView
                                val weatherIconImageView: ImageView = binding.weatherIconImageView
                                val weatherDescriptionTextView: TextView = binding.weatherDescriptionTextView
                                val temperatureTextView: TextView = binding.temperatureTextView
                                main.text = weatherMain
                                cityNameTextView.text = cityName
                                weatherDescriptionTextView.text = weatherDescription
                                temperatureTextView.text = String.format("%.1f°C", temperature)

                                Glide.with(this@MainActivity)
                                    .load(iconUrl)
                                    .override(500, 500)
                                    .into(weatherIconImageView)
                            }
                        } else {
                        }
                    }

                }
            }
        }
        if (hasLocationPermission()) {
            if (isLocationEnabled()) {
                startLocationUpdates()
            } else {
                showEnableLocationDialog()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showEnableLocationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Aktifkan Lokasi")
        dialogBuilder.setMessage("Lokasi tidak diaktifkan. Aktifkan lokasi untuk menggunakan fitur ini.")
        dialogBuilder.setPositiveButton("Pengaturan") { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        dialogBuilder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        getLocation()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Meminta izin lokasi
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}

