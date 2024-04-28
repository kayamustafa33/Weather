package com.kotlin.weather.service

import com.kotlin.weather.BuildConfig
import com.kotlin.weather.api.WeatherApiService
import com.kotlin.weather.model.WeatherForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiClient{

    //Retrofit ile yazılan GET fonksiyonunu çağırıyoruz.
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApiService::class.java)

    // API'den hava durumu bilgilerini almak için kullanılan fonksiyon.
    suspend fun getCurrentWeather(location: String): WeatherForecastResponse {
        return withContext(Dispatchers.IO) {
            retrofit.getWeatherForecast(BuildConfig.API_KEY, location)
        }
    }
}