package com.example.unit_converter

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.unit_converter.databinding.ActivityLengthConversionBinding

class lengthConversion : AppCompatActivity() {
    private lateinit var binding: ActivityLengthConversionBinding

    private val units = arrayOf("Meter", "Kilometer", "Centimeter", "Millimeter", "Foot", "Inch")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLengthConversionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUnits()
        setupConversion()
    }

    private fun setupUnits() {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, units)
        binding.fromUnit.adapter = adapter
        binding.toUnit.adapter = adapter
    }

    private fun setupConversion() {
        binding.btnConvert.setOnClickListener {
            val value = binding.inputValue.text.toString()
            if (value.isEmpty()) {
                binding.resultText.text = "Enter a value!"
                return@setOnClickListener
            }

            val input = value.toDouble()
            val from = binding.fromUnit.selectedItem.toString()
            val to = binding.toUnit.selectedItem.toString()

            val meterValue = when (from) {
                "Kilometer" -> input * 1000
                "Centimeter" -> input / 100
                "Millimeter" -> input / 1000
                "Foot" -> input * 0.3048
                "Inch" -> input * 0.0254
                else -> input
            }

            val result = when (to) {
                "Kilometer" -> meterValue / 1000
                "Centimeter" -> meterValue * 100
                "Millimeter" -> meterValue * 1000
                "Foot" -> meterValue / 0.3048
                "Inch" -> meterValue / 0.0254
                else -> meterValue
            }

            binding.resultText.text = "Result: $result $to"
        }
    }
}