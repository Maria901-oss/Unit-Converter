package com.example.unit_converter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class conversion : AppCompatActivity() {

    private lateinit var inputValue: EditText
    private lateinit var fromUnit: Spinner
    private lateinit var toUnit: Spinner
    private lateinit var resultText: TextView
    private lateinit var btnConvert: Button
    private lateinit var btnSwap: ImageButton
    private var selectedCategory = "Length"
private lateinit var btnCopy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_conversion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        selectedCategory = intent.getStringExtra("category") ?: "Length"
        // Initialize views
        inputValue = findViewById(R.id.inputValue)
        fromUnit = findViewById(R.id.fromUnit)
        toUnit = findViewById(R.id.toUnit)
        resultText = findViewById(R.id.resultText)
        btnConvert = findViewById(R.id.btnConvert)

        loadUnits()

        btnConvert.setOnClickListener {
            convertUnits()
        }
        btnCopy = findViewById(R.id.btnCopy)

        btnCopy.setOnClickListener {
            val text = resultText.text.toString()

            if (text.isNotEmpty()) {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("Result", text)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
            }
        }
        btnSwap = findViewById(R.id.btnSwap)
        btnSwap.setOnClickListener {
            val fromPos = fromUnit.selectedItemPosition
            val toPos = toUnit.selectedItemPosition

            fromUnit.setSelection(toPos)
            toUnit.setSelection(fromPos)

            // Optionally update conversion immediately
            convertUnits()
        }

    }


    private val lengthUnits = mapOf(
        "Meter" to 1.0,
        "Kilometer" to 1000.0,
        "Centimeter" to 0.01,
        "Millimeter" to 0.001,
        "Mile" to 1609.34,
        "Yard" to 0.9144,
        "Feet" to 0.3048,
        "Inch" to 0.0254
    )

    private val volumeUnits = mapOf(
        "Liter" to 1.0,
        "Milliliter" to 0.001,
        "Cubic meter" to 1000.0,
        "Cubic centimeter" to 0.001,
        "Gallon" to 3.78541,
        "Cup" to 0.236588
    )

    private val timeUnits = mapOf(
        "Second" to 1.0,
        "Minute" to 60.0,
        "Hour" to 3600.0,
        "Day" to 86400.0
    )

    private val areaUnits = mapOf(
        "Square meter" to 1.0,
        "Square kilometer" to 1_000_000.0,
        "Square feet" to 0.092903,
        "Square inch" to 0.00064516,
        "Acre" to 4046.86,
        "Hectare" to 10000.0
    )

    private val weightUnits = mapOf(
        "Kilogram" to 1.0,
        "Gram" to 0.001,
        "Milligram" to 0.000001,
        "Pound" to 0.453592,
        "Ounce" to 0.0283495
    )

    private val speedUnits = mapOf(
        "m/s" to 1.0,
        "km/h" to 0.277778,
        "mph" to 0.44704,
        "ft/s" to 0.3048
    )

    private val pressureUnits = mapOf(
        "Pascal" to 1.0,
        "Bar" to 100000.0,
        "PSI" to 6894.76,
        "Atmosphere" to 101325.0
    )

    private val energyUnits = mapOf(
        "Joule" to 1.0,
        "Kilojoule" to 1000.0,
        "Calorie" to 4.184,
        "Kilocalorie" to 4184.0,
        "Watt-hour" to 3600.0
    )

    private val dataUnits = mapOf(
        "Byte" to 1.0,
        "Kilobyte" to 1024.0,
        "Megabyte" to 1024.0 * 1024.0,
        "Gigabyte" to 1024.0 * 1024.0 * 1024.0,
        "Terabyte" to 1024.0 * 1024.0 * 1024.0 * 1024.0
    )

    private val tempUnits = listOf(
        "Celsius",
        "Fahrenheit",
        "Kelvin"
    )


    private fun loadUnits() {

        when (selectedCategory.trim()) {   // ← TRIM FIXES EXTRA SPACES

            "Length" -> setSpinner(lengthUnits.keys.toList())

            "Volume" -> setSpinner(volumeUnits.keys.toList())

            "Area" -> setSpinner(areaUnits.keys.toList())

            "Weight" -> setSpinner(weightUnits.keys.toList())

            "Speed" -> setSpinner(speedUnits.keys.toList())

            "Time" -> setSpinner(timeUnits.keys.toList())

            "Pressure" -> setSpinner(pressureUnits.keys.toList())

            "Energy" -> setSpinner(energyUnits.keys.toList())

            "Data storage" -> setSpinner(dataUnits.keys.toList())

            "Temperature" -> setSpinner(tempUnits)

            else -> setSpinner(lengthUnits.keys.toList())
        }
    }

    private fun setSpinner(list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromUnit.adapter = adapter
        toUnit.adapter = adapter
    }


    private fun convertUnits() {
        val valueStr = inputValue.text.toString()

        if (valueStr.isEmpty()) {
            inputValue.error = "Enter a value"
            return
        }

        val input = valueStr.toDouble()
        val from = fromUnit.selectedItem.toString()
        val to = toUnit.selectedItem.toString()

        // Same Unit
        if (from == to) {
            resultText.text = "$input $to"
            return
        }

        val result = when (selectedCategory) {

            "Temperature" -> convertTemperature(input, from, to)

            "Length" -> input * (lengthUnits[from]!! / lengthUnits[to]!!)

            "Volume" -> input * (volumeUnits[from]!! / volumeUnits[to]!!)

            "Time" -> input * (timeUnits[from]!! / timeUnits[to]!!)

            "Area" -> input * (areaUnits[from]!! / areaUnits[to]!!)

            "Weight" -> input * (weightUnits[from]!! / weightUnits[to]!!)

            "Speed" -> input * (speedUnits[from]!! / speedUnits[to]!!)

            "Pressure" -> input * (pressureUnits[from]!! / pressureUnits[to]!!)

            "Energy" -> input * (energyUnits[from]!! / energyUnits[to]!!)

            "Data storage" -> input * (dataUnits[from]!! / dataUnits[to]!!)

            else -> 0.0
        }

        resultText.text = "%.5f $to".format(result)
    }


    private fun convertTemperature(value: Double, from: String, to: String): Double {

        // Convert to Celsius first
        val celsius = when (from) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> value
        }

        // Convert Celsius to target unit
        return when (to) {
            "Celsius" -> celsius
            "Fahrenheit" -> (celsius * 9 / 5) + 32
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.setting->{
                Toast.makeText(this,"Setting clicked",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.rate->{
                Toast.makeText(this,"Opening Play Store",Toast.LENGTH_SHORT).show()
                try {
                    val uri = Uri.parse("market://details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    // If Play Store not available, open browser
                    val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                true
            }
            R.id.help->{
                Toast.makeText(this,"FAQS clicked",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
