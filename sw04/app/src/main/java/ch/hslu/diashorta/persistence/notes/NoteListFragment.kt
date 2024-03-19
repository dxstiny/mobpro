package ch.hslu.diashorta.persistence.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.hslu.diashorta.persistence.R

/**
 * A fragment representing a list of Items.
 */
class NoteListFragment : Fragment() {

    private val database: NotesDatabase by lazy {
        NotesDatabase.getNotesDatabase(requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                val notes = database.noteDao().all().toTypedArray()
                adapter = NotesAdapter(notes, object : NotesAdapter.OnItemClickListener {
                    override fun onItemClick(id: Int) {
                        val fragment = ShowNoteFragment.newInstance(id)
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.mainActivityView, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                })
            }
        }
        return view
    }
}