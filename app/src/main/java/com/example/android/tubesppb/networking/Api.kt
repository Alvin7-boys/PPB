package com.example.android.tubesppb.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun createWeatherService(): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}