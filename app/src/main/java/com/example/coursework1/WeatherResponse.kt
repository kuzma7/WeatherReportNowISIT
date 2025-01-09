package com.example.weatherapp.com.example.coursework1

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherCondition>,
    val wind: Wind
)
data class Main(
    val temp: Double,
    val humidity: Int
)
data class WeatherCondition(
    val main: String
)
data class Wind(
    val speed: Double
)