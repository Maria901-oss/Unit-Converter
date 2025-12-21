package com.example.unit_converter

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.appbar.MaterialToolbar

class settings : AppCompatActivity() {

    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var numberFormatRadioGroup: RadioGroup
    private lateinit var decimalSeekBar: SeekBar
    private lateinit var decimalValueText: TextView

    private val PREFS_NAME = "AppSettings"
    private val KEY_THEME_MODE = "theme_mode"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.settingsToolbar)
        toolbar.setNavigationOnClickListener { finish() }

        themeRadioGroup = findViewById(R.id.themeRadioGroup)
        numberFormatRadioGroup = findViewById(R.id.numberFormatRadioGroup)
        decimalSeekBar = findViewById(R.id.decimalSeekBar)
        decimalValueText = findViewById(R.id.decimalValueText)

        loadSettings()

        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.themeDefault ->
                    saveThemeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

                R.id.themeLight ->
                    saveThemeMode(AppCompatDelegate.MODE_NIGHT_NO)

                R.id.themeDark ->
                    saveThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        numberFormatRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.formatGeneral -> saveNumberFormat("general")
                R.id.formatThousands -> saveNumberFormat("thousands")
                R.id.formatScientific -> saveNumberFormat("scientific")
            }
        }

        decimalSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                decimalValueText.text = progress.toString()
                saveDecimalPlaces(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun saveThemeMode(mode: Int) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply()

        if (AppCompatDelegate.getDefaultNightMode() != mode) {
            AppCompatDelegate.setDefaultNightMode(mode)
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

        when (prefs.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) {
            AppCompatDelegate.MODE_NIGHT_YES -> themeRadioGroup.check(R.id.themeDark)
            AppCompatDelegate.MODE_NIGHT_NO -> themeRadioGroup.check(R.id.themeLight)
            else -> themeRadioGroup.check(R.id.themeDefault)
        }

        when (prefs.getString("numberFormat", "general")) {
            "general" -> numberFormatRadioGroup.check(R.id.formatGeneral)
            "thousands" -> numberFormatRadioGroup.check(R.id.formatThousands)
            "scientific" -> numberFormatRadioGroup.check(R.id.formatScientific)
        }

        val decimal = prefs.getInt("decimalPlaces", 2)
        decimalSeekBar.progress = decimal
        decimalValueText.text = decimal.toString()
    }
}
