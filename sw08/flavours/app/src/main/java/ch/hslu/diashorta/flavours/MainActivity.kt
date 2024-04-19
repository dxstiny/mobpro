package ch.hslu.diashorta.flavours

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val img = findViewById<ImageView>(R.id.imageView)!!
        val btn = findViewById<ToggleButton>(R.id.toggleButton)!!
        btn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                img.setImageResource(R.drawable.on)
            } else {
                img.setImageResource(R.drawable.off)
            }
        }
        img.setImageResource(R.drawable.off)
        findViewById<TextView>(R.id.tvAppId)!!.text = BuildConfig.APPLICATION_ID
        findViewById<TextView>(R.id.tvVersion)!!.text = BuildConfig.VERSION_NAME
        findViewById<TextView>(R.id.tvName)!!.text = getString(R.string.app_name)
    }
}