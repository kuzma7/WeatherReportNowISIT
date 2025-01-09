package com.example.coursework1

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AutorsActivity : AppCompatActivity() {
    private lateinit var layout: RelativeLayout
    private lateinit var cityName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.autors_layout)

        layout = findViewById(R.id.about_layout)
        cityName = findViewById(R.id.name_text)

        val weatherCondition = intent.getStringExtra("weatherCondition") ?: "clear"

        changeBackground(weatherCondition)

        val exitButton = findViewById<ImageButton>(R.id.exit_button)
        exitButton.setOnClickListener {
            finish()
        }

        val text = "WeatherReportNow"
        val spannableString = SpannableString(text)

        spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#3F8FD2")), 13, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        cityName.text = spannableString

        val authorsTextView = findViewById<TextView>(R.id.authors_text)
        authorsTextView.text = "Разработчики приложения:\n" +
                "\n" +
                "Студенты ВГУВТа" +
                "\n\n" +
                "Романовский Даниил\n" +
                "Гуляев Артем\n" +
                "Кузьминов Никита"
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
}