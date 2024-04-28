package com.kotlin.weather.model

data class WeatherForecastResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)