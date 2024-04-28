package com.kotlin.weather.contracts

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout

interface WeatherImplementor {
    //View modelimiz için oop'yi daha etkili kullanarak implementor interface oluşturduk ve kalıtım aldık
    fun getWeatherDataFromViewModel(context: Context,location : String,weatherIcon : ImageView,layout: LinearLayout)
}