package com.example.unit_converter

import android.content.Intent
import android.health.connect.datatypes.units.Pressure
import android.health.connect.datatypes.units.Volume
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Length=findViewById<Button>(R.id.length)
        val Area=findViewById<Button>(R.id.area)
        val Volume=findViewById<Button>(R.id.volume)
        val Temp=findViewById<Button>(R.id.temp)
        val Energy=findViewById<Button>(R.id.energy)
        val Storage=findViewById<Button>(R.id.data)
        val Speed=findViewById<Button>(R.id.speed)
        val Pressure=findViewById<Button>(R.id.pressure)
        val Weight=findViewById<Button>(R.id.weight)
        val Time=findViewById<Button>(R.id.time)
        Length.setOnClickListener { openConverter("Length") }
        Area.setOnClickListener { openConverter("Area") }
        Time.setOnClickListener { openConverter("Time") }
        Pressure.setOnClickListener { openConverter("Pressure") }
        Weight.setOnClickListener { openConverter("Weight") }
        Speed.setOnClickListener { openConverter("Speed") }
        Storage.setOnClickListener { openConverter("Storage") }
        Energy.setOnClickListener { openConverter("Energy") }
        Volume.setOnClickListener { openConverter("Volume") }
        Temp.setOnClickListener { openConverter("Temp") }
    }
    private fun openConverter(category: String){
    val intent= Intent(this, lengthconversion::class.java)
        intent.putExtra("category",category)
        startActivity(intent)
    }
}