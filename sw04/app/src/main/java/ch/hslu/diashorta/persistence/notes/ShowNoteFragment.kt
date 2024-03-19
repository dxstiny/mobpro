package ch.hslu.diashorta.persistence.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.hslu.diashorta.persistence.databinding.FragmentEditNoteBinding
import ch.hslu.diashorta.persistence.databinding.FragmentShowNoteBinding
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [ShowNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowNoteFragment(id: Int) : Fragment() {
    private val notesDb by lazy {
        NotesDatabase.getNotesDatabase(requireContext().applicationContext)
    }

    private val id = id

    private var _binding: FragmentShowNoteBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(id: Int): ShowNoteFragment {
            return ShowNoteFragment(id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note = notesDb.noteDao().byId(id)

        if (note == null) {
            return
        }

        binding.title.text = note.title
        binding.content.text = note.content
        binding.modified.text = note.modified.toString()
    }
}