package ch.hslu.diashorta.buildtypes

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var icon: ImageView
    private lateinit var toggleButton: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        icon = findViewById(R.id.imageView)!!
        icon.setImageResource(R.mipmap.state_off)
        toggleButton = findViewById(R.id.toggleButton)!!
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                icon.setImageResource(R.mipmap.state_on)
            } else {
                icon.setImageResource(R.mipmap.state_off)
            }
        }
        findViewById<TextView>(R.id.tvName)!!.text = getString(R.string.app_name)
        findViewById<TextView>(R.id.tvVersion)!!.text = BuildConfig.VERSION_NAME
        findViewById<TextView>(R.id.tvAppId)!!.text = BuildConfig.APPLICATION_ID

    }
}