package ch.hslu.diashorta.persistence.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.hslu.diashorta.persistence.R
import ch.hslu.diashorta.persistence.databinding.FragmentEditNoteBinding
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [EditNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditNoteFragment : Fragment() {
    private val notesDb by lazy {
        NotesDatabase.getNotesDatabase(requireContext().applicationContext)
    }

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): EditNoteFragment {
            return EditNoteFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.setOnClickListener { onClickSaveNote() }
    }

    private fun onClickSaveNote() {
        val note = Note()
        note.title = binding.title.text.toString()
        note.content = binding.content.text.toString()
        note.modified = Date(System.currentTimeMillis())
        notesDb.noteDao().insertNotes(note)
        parentFragmentManager.popBackStack()
    }
}