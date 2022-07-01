package com.ananjay.noteitdown.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ananjay.noteitdown.R
import com.ananjay.noteitdown.databinding.FragmentNoteBinding
import com.ananjay.noteitdown.databinding.FragmentRegisterBinding
import com.ananjay.noteitdown.models.NoteRequest
import com.ananjay.noteitdown.models.NoteResponse
import com.ananjay.noteitdown.utils.NetworkResult
import com.ananjay.noteitdown.viewmodel.NoteViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        clickListeners()
        observeData()
    }

    private fun observeData() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success -> {
                    binding.progressNote.visibility = View.GONE
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {
                    binding.progressNote.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    binding.progressNote.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun clickListeners(){
        binding.btnDelete.setOnClickListener {
            //if note exist then only he can delete it
            note?.let {
                noteViewModel.deleteNote(it._id)
            }
        }
        binding.btnSubmit.setOnClickListener {

            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            if(title.isNullOrEmpty() || description.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Fields are mandatory and cannot be empty", Toast.LENGTH_SHORT).show()
            }else{
                val noteRequest = NoteRequest(description, title)
                if(note == null){
                    noteViewModel.createNote(noteRequest)
                }else{
                    noteViewModel.updateNote(note!!._id, noteRequest)
                }
            }

        }
    }

    private fun initView() {
        val jsonNote = arguments?.getString("note")
        if(jsonNote == null){
            binding.tvHeading.text = "Add Note"
            binding.btnDelete.visibility = View.GONE
        }else{
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
            }
        }


    }

}