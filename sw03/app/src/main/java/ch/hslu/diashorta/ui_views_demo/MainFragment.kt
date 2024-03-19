package ch.hslu.diashorta.ui_views_demo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.hslu.diashorta.ui_views_demo.databinding.FragmentMainBinding

class MainFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // avoid handling during init
    private var firstSpinnerSelection = true

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

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

        binding.mainSpinner.onItemSelectedListener = this
        setupRatingBar()
        binding.mainButtonDialog.setOnClickListener { createChoiceDialog().show() }
        binding.layoutLinearSelected.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mainActivityView, LinearLayoutFragment())
                .addToBackStack("Linear Layout Fragment")
                .commit()
        }

        binding.layoutConstraintSelected.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.mainActivityView, ConstraintLayoutFragment())
                .addToBackStack("Constraint Layout Fragment")
                .commit()
        }
    }

    private fun setupRatingBar() {
        binding.mainRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.mainRatingBarText.text = getString(R.string.aha_you_chose, rating)
        }
        binding.mainRatingBar.rating = 3.0f
    }

    private fun createChoiceDialog(): Dialog {
        val items = arrayOf(getString(R.string.everything),
                            getString(R.string.something),
                            getString(R.string.nothing))
        val builder = AlertDialog.Builder(activity)
            .setTitle(getString(R.string.what_do_you_want))
            .setItems(items) { _, which ->
                val msg = getString(R.string.you_chose, items[which])
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            }.setNegativeButton(getString(R.string.dont_know)) { _, _ ->
                /* ignore */
            }
        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        firstSpinnerSelection = true
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (firstSpinnerSelection) {
            firstSpinnerSelection = false
            return
        }
        val msg = "Du hast ${parent?.getItemAtPosition(position)} gewählt."
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        /* ignore */
    }
}