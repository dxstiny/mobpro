package ch.hslu.diashorta.sw07

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MyMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("MyMapActivity", "onCreate")

        val geo = intent.data.toString()

        val tvLat: TextView = findViewById(R.id.tvLat)
        val tvLong: TextView = findViewById(R.id.tvLon)

        val coord = geo.split(":")[1].split(",")

        tvLat.text = "Latitude: ${coord[0]}"
        tvLong.text = "Longitude: ${coord[1]}"
    }
}