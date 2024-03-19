package ch.hslu.diashorta.persistence.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.diashorta.persistence.R

class NotesAdapter (private val dataSet: Array<Note>, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = dataSet[position]
        holder.view.findViewById<TextView>(R.id.note_title).text = item.title
        holder.view.setOnClickListener { onItemClickListener.onItemClick(item.id) }
    }

    override fun getItemCount(): Int = dataSet.size

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }
}