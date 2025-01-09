package com.example.coursework1

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.com.example.coursework1.WeatherResponse
import com.example.weatherapp.com.example.coursework1.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var layout: RelativeLayout
    private lateinit var cityInput: EditText
    private lateinit var getWeatherButton: Button
    private lateinit var weatherInfo: TextView
    private lateinit var cityName: TextView
    private lateinit var weatherImage: ImageView
    private lateinit var dateTime: TextView

    private lateinit var lastWeatherCondition: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        layout = findViewById(R.id.main_layout)
        cityInput = findViewById(R.id.city_input)
        getWeatherButton = findViewById(R.id.get_weather_button)
        weatherInfo = findViewById(R.id.weather_info)
        cityName = findViewById(R.id.city_name)
        weatherImage = findViewById(R.id.weather_image)
        dateTime = findViewById(R.id.date_time)

        lastWeatherCondition = "clear"
        val textViewName: TextView = findViewById(R.id.name_text)
        val text = "WeatherReportNow"
        val spannableString = SpannableString(text)

        spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#3F8FD2")), 13, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textViewName.text = spannableString

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)

        getWeatherButton.setOnClickListener {
            val city = cityInput.text.toString()
            if (city.isNotEmpty()) {
                getWeather(service, city)
            } else {
                Toast.makeText(this, "Пожалуйста, введите название города.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val aboutButton = findViewById<Button>(R.id.about_button)
        aboutButton.setOnClickListener {
            val weatherCondition = lastWeatherCondition
            val intent = Intent(this, AutorsActivity::class.java)
            intent.putExtra("weatherCondition", weatherCondition)
            startActivity(intent)
        }

        val restartButton: Button = findViewById(R.id.reset_button)
        restartButton.setOnClickListener {
            restartApp()
        }
    }

    private fun restartApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
    private fun getWeather(service: WeatherService, city: String) {
        val apiKey = "937c7e3a3a02d3ccba42416631ef7fcf"
        service.getWeather(city, apiKey).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val weatherResponse = response.body()!!
                    val weatherCondition = weatherResponse.weather[0].main
                    lastWeatherCondition = weatherCondition
                    val weatherConditionForText = translateWeatherCondition(weatherCondition)
                    val temperature = weatherResponse.main.temp - 273.15
                    val temperatureText = String.format("%.1f", temperature)
                    val windSpeed = weatherResponse.wind.speed
                    val humidity = weatherResponse.main.humidity


                    weatherInfo.text = "Температура: $temperatureText°C\n" +
                            "Погода: $weatherConditionForText\n" +
                            "Скорость ветра: $windSpeed м/с\n" +
                            "Влажность: $humidity%"

                    dateTime.text = getCurrentDateTime()
                    cityName.text = cityInput.text.toString()
                    changeBackground(weatherCondition)
                    showWeatherImage(weatherCondition)
                } else {
                    Toast.makeText(this@MainActivity, "Пожалуйста, введите корректное название города.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Пожалуйста, введите корректное название города.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getCurrentDateTime(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd MM yyyy, HH:mm", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun changeBackground(weatherCondition: String) {
        when (weatherCondition.lowercase()) {
            "clear" -> layout.setBackgroundResource(R.drawable.clear_weather)
            "clouds" -> layout.setBackgroundResource(R.drawable.cloud_weather)
            "rain" -> layout.setBackgroundResource(R.drawable.rain_weather)
            "snow" -> layout.setBackgroundResource(R.drawable.snow_weather)
            else -> layout.setBackgroundResource(R.drawable.clear_weather)
        }
    }

    private fun showWeatherImage(weatherCondition: String) {
        when (weatherCondition.lowercase()) {
            "clear" -> weatherImage.setImageResource(R.drawable.sun)
            "clouds" -> weatherImage.setImageResource(R.drawable.cloud)
            "rain" -> weatherImage.setImageResource(R.drawable.rain)
            "snow" -> weatherImage.setImageResource(R.drawable.snow)
            else -> weatherImage.setImageResource(R.drawable.sun)
        }
    }

    private fun translateWeatherCondition(condition: String): String {
        return when (condition) {
            "Clear" -> "Ясно"
            "Clouds" -> "Облачно"
            "Rain" -> "Дождь"
            "Snow" -> "Снег"
            else -> condition
        }
    }
}
