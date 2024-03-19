package ch.hslu.diashorta.ui_views_demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ch.hslu.diashorta.ui_views_demo.databinding.FragmentConstraintLayoutBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ConstraintLayoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConstraintLayoutFragment : Fragment() {
    private var counter = 0
    private val counterViewModel: CounterViewModel by activityViewModels()

    private var _binding: FragmentConstraintLayoutBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConstraintLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModelCounterButton()
        setupFragmentCounterButton()
    }

    private fun setupViewModelCounterButton() {
        binding.btnIncViewModel.text = counterViewModel.getButtonName()
        binding.btnIncViewModel.setOnClickListener {
            counterViewModel.increment()
            binding.btnIncViewModel.text = counterViewModel.getButtonName()
        }
    }

    private fun setupFragmentCounterButton() {
        binding.btnIncFragment.text = "Fragment: $counter++"
        binding.btnIncFragment.setOnClickListener {
            counter++
            binding.btnIncFragment.text = "Fragment: $counter++"
        }
    }
}