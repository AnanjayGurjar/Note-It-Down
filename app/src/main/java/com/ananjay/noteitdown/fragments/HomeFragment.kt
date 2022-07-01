package com.ananjay.noteitdown.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ananjay.noteitdown.R
import com.ananjay.noteitdown.adatpters.NoteAdapter
import com.ananjay.noteitdown.databinding.FragmentHomeBinding
import com.ananjay.noteitdown.models.NoteResponse
import com.ananjay.noteitdown.utils.NetworkResult
import com.ananjay.noteitdown.viewmodel.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()

    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteItemClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNoteResponse()
        noteViewModel.getNotes()
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvNotes.adapter = adapter

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_noteFragment)
        }
    }

    private fun observeNoteResponse(){
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                    Log.d(TAG, "observeNoteResponse: ${it.data}")
                    showProgressBar(binding.noteProgressBar, false)
                }
                is NetworkResult.Error -> {
                    Log.d(TAG, "observeNoteResponse: error ${it.message}")
                    showProgressBar(binding.noteProgressBar, false)
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {
                    Log.d(TAG, "observeNoteResponse: loading")
                    showProgressBar(binding.noteProgressBar, true)
                }
            }
        }
    }

    private fun showProgressBar(progressBar: ProgressBar, makeVisible: Boolean){
        //TODO: Create a base Fragment class and move it there
        if(makeVisible){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

    private fun onNoteItemClicked(noteResponse: NoteResponse){
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_homeFragment_to_noteFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}