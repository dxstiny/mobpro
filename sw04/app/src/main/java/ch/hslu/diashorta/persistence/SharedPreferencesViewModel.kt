package ch.hslu.diashorta.persistence

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class SharedPreferencesViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val PREF_TEA_PREFERRED = "teaPreferred"
        private const val PREF_TEA_WITH_SUGAR = "teaWithSugar"
        private const val PREF_TEA_SWEETENER = "teaSweetener"

        private const val PREF_TEA_PREFERRED_DEFAULT = "Meßmer/Schwarztee"
        private const val PREF_TEA_WITH_SUGAR_DEFAULT = false
    }

    private var preferencesSummary = MutableLiveData<String>()

    fun getPreferencesSummary(): LiveData<String> {
        return preferencesSummary
    }

    private val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(getApplication<Application>().applicationContext)
    }

    init {
        buildPreferencesSummaryString()
    }

    fun setDefaultPreferences() {
        val editor = preferences.edit()
        editor.putBoolean(PREF_TEA_WITH_SUGAR, PREF_TEA_WITH_SUGAR_DEFAULT)
        editor.putString(PREF_TEA_SWEETENER, null)
        editor.putString(PREF_TEA_PREFERRED, PREF_TEA_PREFERRED_DEFAULT)
        editor.apply()
        buildPreferencesSummaryString()
    }

    fun buildPreferencesSummaryString() {
        val summary = StringBuilder()
        summary.append("Ich trinke am liebsten ")
        summary.append(preferences.getString(PREF_TEA_PREFERRED, PREF_TEA_PREFERRED_DEFAULT))
        summary.append(", ")
        if (preferences.getBoolean(PREF_TEA_WITH_SUGAR, PREF_TEA_WITH_SUGAR_DEFAULT)) {
            summary.append("mit ")
            summary.append(getSweetenerDisplayValue())
        } else {
            summary.append("ohne Zucker")
        }
        preferencesSummary.value = summary.toString()
    }

    private fun getSweetenerDisplayValue(): String? {
        val sweetenerValues = getApplication<Application>().resources.getStringArray(R.array.teaSweetenerValues)
        val sweetenerEntries = getApplication<Application>().resources.getStringArray(R.array.teaSweetener)
        val sweetenerValue = preferences.getString(PREF_TEA_SWEETENER, null)
        val sweetenerIndex = sweetenerValues.indexOf(sweetenerValue)
        if (sweetenerIndex == -1) {
            return null
        }
        return sweetenerEntries[sweetenerIndex]
    }
}
