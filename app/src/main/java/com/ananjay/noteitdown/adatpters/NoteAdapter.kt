package com.ananjay.noteitdown.adatpters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ananjay.noteitdown.adatpters.NoteAdapter.*
import com.ananjay.noteitdown.databinding.NoteItemBinding
import com.ananjay.noteitdown.models.NoteResponse

class NoteAdapter(): ListAdapter<NoteResponse, NoteViewHolder>(ComparatorDiffUtil()) {

    inner class NoteViewHolder(private val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(note: NoteResponse){
            binding.tvNoteTitle.text = note.title
            binding.tvNoteDescription.text = note.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        note.let {
            holder.bind(it)
        }
    }

    class ComparatorDiffUtil: DiffUtil.ItemCallback<NoteResponse>(){
        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
            return oldItem == newItem
        }

    }
}