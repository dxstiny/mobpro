package ch.hslu.diashorta.sw07

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.DialogCompat
import ch.hslu.diashorta.sw07.databinding.ActivityMainBinding
import java.util.Date


private const val MY_ACTION_SHOW_TEXT = "ch.hslu.mobpro.actions.SHOW_TEXT"
private const val MY_EXTRA_KEY = "text"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        if (intent.action.contentEquals(MY_ACTION_SHOW_TEXT)) {
            binding.tvIntent.text = intent.getStringExtra(MY_EXTRA_KEY)
        }

        val lat = 37.7749
        val lng = -122.4194
        val uri = "geo:$lat,$lng"
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        binding.btnSendIntent.setOnClickListener {
            startActivity(mapIntent)
        }

        binding.btnGetAllIntentReceivers.setOnClickListener {
            val activities = packageManager.queryIntentActivities(mapIntent, 0)

            AlertDialog.Builder(this)
                .setTitle("show apps that can handle this intent")
                .setMessage(activities.joinToString("\n") { it.activityInfo.packageName })
                .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
                .create()
                .show()
        }

        binding.btnStartMyAction.setOnClickListener {
            val intent = Intent("ch.hslu.mobpro.actions.SHOW_TEXT")
                .addCategory(Intent.CATEGORY_DEFAULT)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .putExtra(MY_EXTRA_KEY, """
                    Activity gestartet durch folgende Intent-ACTION:
                    Action: $MY_ACTION_SHOW_TEXT
                    Jetzt: ${Date()}""".trimIndent())
            startActivity(intent)
        }

        binding.btnUpdateWidgetText.setOnClickListener {
            val prefs = getSharedPreferences(MyAppWidget.MY_PREFS_NAME, MODE_PRIVATE)
            val editor = prefs.edit()
            val text = binding.etWidgetText.text.toString()
            editor.putString(MyAppWidget.WIDGET_TEXT_PREFS_KEY, text)
            editor.apply()

            val widget = ComponentName(this, MyAppWidget::class.java)
            val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(widget)

            Log.d("MainActivity", "ids: ${ids.joinToString()}")

            val updateWidget = Intent(this, MyAppWidget::class.java)
            updateWidget.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            updateWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            sendBroadcast(updateWidget)
        }
    }
}