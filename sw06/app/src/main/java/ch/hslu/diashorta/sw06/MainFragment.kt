package ch.hslu.diashorta.sw06

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkInfo
import ch.hslu.diashorta.sw06.databinding.FragmentMainBinding
import ch.hslu.diashorta.sw06.musicPlayer.MusicPlayerConnection
import ch.hslu.diashorta.sw06.musicPlayer.MusicPlayerService
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    private var musicPlayerBroadcastReceiver: MusicPlayerBroadcastReceiver? = null
    private var musicPlayerConnection: MusicPlayerConnection? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val INTENT_FILTER = "ACTION_MY_BROADCAST"
        private const val PACKAGE_NAME = "ch.hslu.diashorta.sw06"
    }

    private val musicPlayerIntent: Intent by lazy {
        Intent(requireActivity(), MusicPlayerService::class.java)
    }
    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            binding.btnStartMusicPlayer.isEnabled = true
            startMusicPlayer()
        } else {
            binding.btnStartMusicPlayer.isEnabled = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartMusicPlayer.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPushNotificationPermission()
            }
        }
        binding.btnStopMusicPlayer.setOnClickListener {
            requireActivity().stopService(musicPlayerIntent)
            binding.cbServiceConnected.isEnabled = false
            binding.btnStopMusicPlayer.isEnabled = false
            binding.btnStartMusicPlayer.isEnabled = true
        }
        binding.btnSkipTrack.setOnClickListener {
            musicPlayerConnection?.let {
                val nextTrack = it.playNext()
                Toast.makeText(requireContext(), "Playing $nextTrack", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnHistory.setOnClickListener {
            musicPlayerConnection?.let {
                val history = it.queryHistory()
                val historyString = history.joinToString("\n")
                AlertDialog.Builder(requireContext())
                    .setTitle("History")
                    .setMessage(historyString)
                    .setNeutralButton("Close", null)
                    .create()
                    .show()
            }
        }
        binding.cbServiceConnected.setOnCheckedChangeListener { _, isChecked ->
            binding.btnSkipTrack.isEnabled = isChecked
            binding.btnHistory.isEnabled = isChecked
            if (isChecked) {
                bindMusicPlayerService()
            } else {
                unbindMusicPlayerService()
            }
        }
        binding.cbBroadcastRegistered.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                musicPlayerBroadcastReceiver = MusicPlayerBroadcastReceiver()
                ContextCompat.registerReceiver(
                    requireContext(),
                    musicPlayerBroadcastReceiver,
                    android.content.IntentFilter(INTENT_FILTER),
                    RECEIVER_NOT_EXPORTED
                )
            } else {
                requireActivity().unregisterReceiver(musicPlayerBroadcastReceiver)
            }
        }
        binding.btnBroadcast.setOnClickListener {
            val localBroadcast = Intent(INTENT_FILTER)
            localBroadcast.setPackage(PACKAGE_NAME)
            requireActivity().sendBroadcast(localBroadcast)
        }
        binding.btnStartWorkManager.setOnClickListener {
            val workManager = androidx.work.WorkManager.getInstance(requireContext())
            val workRequest = androidx.work.OneTimeWorkRequestBuilder<LocaliseMissilesWorker>()
                .build()
            workManager.enqueue(workRequest)

            val workInfoData = workManager.getWorkInfoByIdFlow(workRequest.id)
            lifecycleScope.launch {
                workInfoData.collect {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        val positions = it.outputData.getStringArray("missilePositions")
                        positions?.let {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Missile positions")
                                .setMessage(it.joinToString("\n"))
                                .setNeutralButton("Close", null)
                                .create()
                                .show()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPushNotificationPermission() {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        val res = requireActivity().checkSelfPermission(permission)
        if (res == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            binding.btnStartMusicPlayer.isEnabled = true
            startMusicPlayer()
            return
        }

        pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun bindMusicPlayerService() {
        musicPlayerConnection = MusicPlayerConnection()
        requireActivity().bindService(musicPlayerIntent, musicPlayerConnection!!, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicPlayerService() {
        musicPlayerConnection?.let {
            requireActivity().unbindService(it)
            musicPlayerConnection = null
        }
    }

    private fun startMusicPlayer() {
        requireActivity().startService(musicPlayerIntent)
        Toast.makeText(requireContext(), "Music player started", Toast.LENGTH_SHORT).show()
        binding.cbServiceConnected.isEnabled = true
        binding.btnStopMusicPlayer.isEnabled = true
        binding.btnStartMusicPlayer.isEnabled = false
    }

    private inner class MusicPlayerBroadcastReceiver : android.content.BroadcastReceiver() {
        private var receivedBroadcasts = 0

        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            receivedBroadcasts++
            this@MainFragment.binding.tvBroadcastMessage.text = "Received $receivedBroadcasts broadcasts"
        }
    }
}
