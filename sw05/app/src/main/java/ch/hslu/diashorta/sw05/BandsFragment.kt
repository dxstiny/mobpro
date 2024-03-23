package ch.hslu.diashorta.sw05

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ch.hslu.diashorta.sw05.databinding.FragmentBandsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class BandsFragment : Fragment() {

    private var _binding: FragmentBandsBinding? = null
    private val binding get() = _binding!!

    private val bandsViewModel: BandsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBandsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRequest.setOnClickListener {
            bandsViewModel.requestBands()
        }
        binding.btnShowBand.setOnClickListener {
            showBandsDialog(bandsViewModel.bands)
        }
        binding.btnResetViewModel.setOnClickListener {
            bandsViewModel.reset()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bandsViewModel.bandsFlow.collect { bands ->
                    binding.btnShowBand.isEnabled = bands.isNotEmpty()
                    binding.txtCurrentBandName.text = ""
                    binding.txtCurrentBandInfo.text = ""
                    binding.imgCurrentBand.visibility = View.GONE
                    binding.txtNumberOfBands.text = "Anzahl Bands: ${bands.size}"
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bandsViewModel.currentBand.collect { bandInfo ->
                    bandInfo?.let {
                        binding.txtCurrentBandName.text = it.name
                        binding.txtCurrentBandInfo.text = "Gegründet: ${it.foundingYear}\n" +
                                "Heimatland: ${it.homeCountry}"

                        if (bandInfo.bestOfCdCoverImageUrl != null) {
                            Picasso.get()
                                .load(bandInfo.bestOfCdCoverImageUrl)
                                .into(binding.imgCurrentBand)
                            binding.imgCurrentBand.visibility = View.VISIBLE
                        } else {
                            binding.imgCurrentBand.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun showBandsDialog(bands: List<BandCode>) {
        AlertDialog.Builder(requireContext())
            .setTitle("Welche Band?")
            .setItems(bands.map { bandCode -> bandCode.name }.toTypedArray())
            { _, which: Int ->
                bandsViewModel.requestBandInfo(bands[which].code)
            }
            .setNegativeButton("Abbrechen") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}