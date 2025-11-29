package com.example.unit_converter

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class lengthconversion : AppCompatActivity() {
    private lateinit var inputValue: EditText
    private lateinit var fromUnit: Spinner
    private lateinit var toUnit: Spinner
    private lateinit var resultText: TextView
    private lateinit var btnCopy: Button
    private lateinit var btnConvert: Button
    private var selectedCategory="Length"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lengthconversion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        selectedCategory=intent.getStringExtra("category")?:"Length"
        inputValue=findViewById(R.id.inputValue)
        fromUnit=findViewById(R.id.fromUnit)
        toUnit=findViewById(R.id.toUnit)
        resultText=findViewById(R.id.resultText)
        btnCopy=findViewById(R.id.btnCopy)
        btnConvert=findViewById(R.id.btnConvert)
        loadUnits()
        btnConvert.setOnClickListener { convertUnits() }
    }
    private val lengthUnits=mapOf(
        "Meter" to 1.0,
        "Centimeter" to 0.01,
        "Millimeter" to 0.001,
        "Kilometer" to 1000.0,
        "Feet" to 0.3084,
        "Inch" to 0.0254,
        "Mile" to 1609.34,
        "Yard" to 0.9144,
    )
    private val volumeUnits=mapOf(
        "Liter" to 1.0,
        "Cubic centimeter" to 0.001,
        "Milliliter" to 0.001,
        "Cubic meter" to 1000.0,
        "Gallon" to 3.78541,
        "Cup" to 0.236588,
    )
    private val timeUnits=mapOf(
        "Second" to 1.0,
        "Minute" to 60.0,
        "Hour" to 3600.0,
        "Day" to 86400.0,
    )
    private val areaUnits=mapOf(
        "Square meter" to 1.0,
        "Square kilometer" to 1000000.0,
        "Square feet" to 0.092903,
        "Square inch" to 0.00064516,
        "Arce" to 4046.86,
        "Hectare" to 10000.0,
    )
    private val weightUnits=mapOf(
        "Kilogram" to 1.0,
        "Milligram" to 0.000001,
        "Gram" to 0.001,
        "Pound" to 0.453592,
        "Ounce" to 0.0283495,
    )
    private val speedUnits=mapOf(
        "m/s" to 1.0,
        "km/h" to 0.277778,
        "mph" to 0.44704,
        "ft/s" to 0.3048,
    )
    private val pressureUnits=mapOf(
        "Pascal" to 1.0,
        "Bar" to 100000.0,
        "PSI" to 6894.76,
        "Atmosphere" to 101325.0,
    )
    private val energyUnits=mapOf(
        "Joule" to 1.0,
        "Kilojoule" to 1000.0,
        "Watt-hour" to 3600.0,
        "Calorie" to 4.184,
        "Kilocalorie" to 4184.0,
    )
    private val dataUnits=mapOf(
        "Byte" to 1.0,
        "Terabyte" to 1099511627776.0,
        "Megabyte" to 1048576.0,
        "Kilobyte" to 1024.0,
        "Gigabyte" to 1073741824.0,
    )
    private val tempUnits=listOf(
        "Kelvin",
        "Celsius",
        "Fahrenheit",
    )
    private fun loadUnits(){
        val units= when(selectedCategory){
            "Length"->lengthUnits.keys.toList()
            "Volume"->volumeUnits.keys.toList()
            "Time"->timeUnits.keys.toList()
            "Area"->areaUnits.keys.toList()
            "Pressure"->pressureUnits.keys.toList()
            "Weight"->weightUnits.keys.toList()
            "Energy"->energyUnits.keys.toList()
            "Data storage"->dataUnits.keys.toList()
            "Speed"->speedUnits.keys.toList()
            "Temperature"->tempUnits
            else -> lengthUnits.keys.toList()

        }
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,units)
        fromUnit.adapter=adapter
        toUnit.adapter=adapter
    }
    private fun convertUnits(){
        val valueStr=inputValue.text.toString()
        if (valueStr.isEmpty()){
            inputValue.error="Enter value"
            return
        }
        val input=valueStr.toDouble()
        val from=fromUnit.selectedItem.toString()
        val to=toUnit.selectedItem.toString()
        if (from == to) {
            resultText.text = "$input $to"
            return
        }

        val result=when(selectedCategory){
            "Temperature"->convertTemperature(input,from,to)
            "Length"->input*(lengthUnits[from]!!/lengthUnits[to]!!)
            "Volume"->input*(volumeUnits[from]!!/volumeUnits[to]!!)
            "Energy"->input*(energyUnits[from]!!/energyUnits[to]!!)
            "Weight"->input*(weightUnits[from]!!/weightUnits[to]!!)
            "Pressure"->input*(pressureUnits[from]!!/pressureUnits[to]!!)
            "Area"->input*(areaUnits[from]!!/areaUnits[to]!!)
            "Time"->input*(timeUnits[from]!!/timeUnits[to]!!)
            "Speed"->input*(speedUnits[from]!!/speedUnits[to]!!)
            "Data storage"->input*(dataUnits[from]!!/dataUnits[to]!!)

            else -> 0.0
        }
        resultText.text = "%.5f $to".format(result)


    }
    private fun convertTemperature(value: Double, from: String, to: String): Double{
        val celsius=when(from){
            "Kelvin"->value-273.15
            "Celsius"->value
            "Fahrenheit"->(value-32.0)*5.0/9.0
            else -> value
        }
        return when(to){
            "Kelvin"->celsius+273.15
            "Celsius"->celsius
            "Fahrenheit"->(celsius*9.0/5.0)+32.0
            else -> celsius
        }
    }

}


