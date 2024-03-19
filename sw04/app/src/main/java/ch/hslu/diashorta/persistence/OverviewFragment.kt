package ch.hslu.diashorta.persistence

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.hslu.diashorta.persistence.databinding.FragmentOverviewBinding
import ch.hslu.diashorta.persistence.notes.EditNoteFragment
import ch.hslu.diashorta.persistence.notes.NoteListFragment

class OverviewFragment : Fragment() {

    companion object {
        private const val SHARED_PREFERENCES_OVERVIEW = "SHARED_PREFERENCES_OVERVIEW"
        private const val COUNTER_KEY = "COUNTER_KEY"
    }

    private var _binding: FragmentOverviewBinding? = null

    private val binding get() = _binding!!

    private val sharedPreferencesViewModel: SharedPreferencesViewModel by activityViewModels()

    private val preferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(SHARED_PREFERENCES_OVERVIEW, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateOnResumeCallsText()

        sharedPreferencesViewModel.getPreferencesSummary().observe(viewLifecycleOwner) {
            binding.overviewTeaPreference.text = it
        }

        binding.overviewShowNote.setOnClickListener {
            showNoteList()
        }
        binding.overviewCreateNote.setOnClickListener {
            createNote()
        }

        binding.overviewTeaEdit.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainActivityView, TeaPreferenceFragment.newInstance())
                .addToBackStack("preferences")
                .commit()
        }
        binding.overviewTeaDefault.setOnClickListener {
            sharedPreferencesViewModel.setDefaultPreferences()
        }
        binding.overviewShowSMS.setOnClickListener {
            showSmsList()
        }
    }

    // APP PERSISTENCY

    override fun onResume() {
        super.onResume()

        val newResumeCount = preferences.getInt(COUNTER_KEY, 0) + 1
        val editor = preferences.edit()
        editor.putInt(COUNTER_KEY, newResumeCount)
        editor.apply()
        updateOnResumeCallsText()
        sharedPreferencesViewModel.buildPreferencesSummaryString()
    }

    private fun updateOnResumeCallsText() {
        val resumeCount = preferences.getInt(COUNTER_KEY, 0)

        binding.overviewOnResumeCalls.text = getString(
            R.string.mainactivity_onresume_wurde_seit_der_installation_dieser_app_mal_aufgerufen,
            resumeCount.toString()
        )
    }

    // CONTENT PROVIDER & PERMISSIONS

    private fun performOperationWithPermission(permission: String) {
        val res = requireActivity().checkSelfPermission(permission)
        val granted = res == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestPermissionLauncher.launch(permission)
        } else {
            showSMS()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showSMS()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSmsList() {
        performOperationWithPermission(Manifest.permission.READ_SMS)
    }

    private fun showSMS() {
        val cursor = requireActivity().contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.BODY),
            null,
            null,
            null
        )

        AlertDialog.Builder(context)
            .setTitle("${cursor?.count} SMS in Inbox")
            .setCursor(cursor, null, Telephony.TextBasedSmsColumns.BODY)
            .setNeutralButton("OK", null)
            .create()
            .show()
    }

    // ROOMDB

    private fun showNoteList() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainActivityView, NoteListFragment())
            .addToBackStack("notes")
            .commit()
    }

    private fun createNote() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainActivityView, EditNoteFragment.newInstance())
            .addToBackStack("notes")
            .commit()
    }
}