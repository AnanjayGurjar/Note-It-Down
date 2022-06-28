package com.ananjay.noteitdown.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ananjay.noteitdown.R
import com.ananjay.noteitdown.databinding.FragmentLoginBinding
import com.ananjay.noteitdown.models.UserRequest
import com.ananjay.noteitdown.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            authViewModel.loginUser(UserRequest("test1234@gmail.com", "123456", "test1234"))
//            authViewModel.registerUser(UserRequest("test12345@gmail.com", "123456", "test1234"))
        }
    }
}