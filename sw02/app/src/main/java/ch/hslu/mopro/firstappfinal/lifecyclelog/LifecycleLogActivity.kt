package ch.hslu.mopro.firstappfinal.lifecyclelog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.mopro.firstappfinal.R


class LifecycleLogActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_lifecycle_logger)
		Log.i("hslu_mobApp", "Activity onCreate() aufgerufen")
		if (savedInstanceState == null) {
			supportFragmentManager
				.beginTransaction()
				.add(android.R.id.content,
					LifecycleLogFragment())
				.commit()
		}
	}

	override fun onActivityReenter(resultCode: Int, data: Intent?) {
		Log.i("hslu_mobApp", "Activity onActivityReenter() aufgerufen")
	}

	override fun onStart() {
		super.onStart()
		Log.i("hslu_mobApp", "Activity onStart() aufgerufen")
	}

	override fun onResume() {
		super.onResume()
		Log.i("hslu_mobApp", "Activity onResume() aufgerufen")
	}

	override fun onPause() {
		super.onPause()
		Log.i("hslu_mobApp", "Activity onPause() aufgerufen")
	}

	override fun onStop() {
		super.onStop()
		Log.i("hslu_mobApp", "Activity onStop() aufgerufen")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.i("hslu_mobApp", "Activity onDestroy() aufgerufen")
	}

	override fun onRestart() {
		super.onRestart()
		Log.i("hslu_mobApp", "Activity onRestart() aufgerufen")
	}

	override fun onNewIntent(intent: Intent?) {
		super.onNewIntent(intent)
		Log.i("hslu_mobApp", "Activity onNewIntent() aufgerufen")
	}

	override fun onContentChanged() {
		super.onContentChanged()
		Log.i("hslu_mobApp", "Activity onContentChanged() aufgerufen")
	}
}