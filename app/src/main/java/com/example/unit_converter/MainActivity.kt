package com.example.unit_converter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
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
        Storage.setOnClickListener { openConverter("Data storage") }
        Energy.setOnClickListener { openConverter("Energy") }
        Volume.setOnClickListener { openConverter("Volume") }
        Temp.setOnClickListener { openConverter("Temperature") }
    }
        private fun openConverter(category: String){
        val intent= Intent (this, conversion::class.java)
        intent.putExtra("category",category)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.setting->{
                val intent= Intent(this, settings::class.java)
                startActivity(intent)
                true
            }
            R.id.rate->{
                Toast.makeText(this,"Opening Play Store",Toast.LENGTH_SHORT).show()
                try {
                    val uri = Uri.parse("market://details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}