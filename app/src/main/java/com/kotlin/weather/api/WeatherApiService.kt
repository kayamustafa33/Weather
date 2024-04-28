package com.kotlin.weather.api

import com.kotlin.weather.BuildConfig
import com.kotlin.weather.model.WeatherForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    //GET isteği ile güncel hava durumu için istek oluşturuyoruz.
    //Parametreleri key,q,days,aqi ve alerts
    @GET(BuildConfig.FORECAST_GET) //Build config verimizi aldık
    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("aqi") includeAqi: String = "no",
        @Query("alerts") includeAlerts: String = "no"
    ): WeatherForecastResponse
}