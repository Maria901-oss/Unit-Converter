package com.example.unit_converter

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat

class settings : AppCompatActivity() {
    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var numberFormatRadioGroup: RadioGroup
    private lateinit var decimalSeekBar: SeekBar
    private lateinit var decimalValueText: TextView
    private val PREFS_NAME = "AppSettings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        themeRadioGroup = findViewById(R.id.themeRadioGroup)
        numberFormatRadioGroup = findViewById(R.id.numberFormatRadioGroup)
        decimalSeekBar = findViewById(R.id.decimalSeekBar)
        decimalValueText = findViewById(R.id.decimalValueText)

        // Load saved settings first
        loadSettings()

        // Theme change listener
        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.themeDefault -> applyTheme("default")
                R.id.themeLight -> applyTheme("light")
                R.id.themeDark -> applyTheme("dark")
            }
        }

        // Number format listener
        numberFormatRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.formatGeneral -> saveNumberFormat("general")
                R.id.formatThousands -> saveNumberFormat("thousands")
                R.id.formatScientific -> saveNumberFormat("scientific")
            }
        }

        // Decimal places SeekBar listener
        decimalSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                decimalValueText.text = progress.toString()
                saveDecimalPlaces(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun applyTheme(theme: String) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putString("theme", theme).apply()

        when (theme) {
            "default" -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "light" -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            "dark" -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    private fun saveNumberFormat(format: String) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putString("numberFormat", format).apply()
    }

    private fun saveDecimalPlaces(value: Int) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putInt("decimalPlaces", value).apply()
    }

    private fun loadSettings() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Theme
        when (prefs.getString("theme", "default")) {
            "default" -> themeRadioGroup.check(R.id.themeDefault)
            "light" -> themeRadioGroup.check(R.id.themeLight)
            "dark" -> themeRadioGroup.check(R.id.themeDark)
        }

        // Number Format
        when (prefs.getString("numberFormat", "general")) {
            "general" -> numberFormatRadioGroup.check(R.id.formatGeneral)
            "thousands" -> numberFormatRadioGroup.check(R.id.formatThousands)
            "scientific" -> numberFormatRadioGroup.check(R.id.formatScientific)
        }

        // Decimal Places
        val decimal = prefs.getInt("decimalPlaces", 2)
        decimalSeekBar.progress = decimal
        decimalValueText.text = decimal.toString()
    }

    fun formatNumber(value: Double): String {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val formatType = prefs.getString("numberFormat", "general") ?: "general"
        val decimals = prefs.getInt("decimalPlaces", 2)

        return when (formatType) {
            "general" -> "%.${decimals}f".format(value)
            "thousands" -> {
                val pattern = "#,##0.${"0".repeat(decimals)}"
                DecimalFormat(pattern).format(value)
            }
            "scientific" -> "%.${decimals}E".format(value)
            else -> value.toString()
        }
    }
}
