package com.ananjay.noteitdown.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ananjay.noteitdown.R
import com.ananjay.noteitdown.databinding.FragmentLoginBinding
import com.ananjay.noteitdown.databinding.FragmentRegisterBinding
import com.ananjay.noteitdown.models.UserRequest
import com.ananjay.noteitdown.utils.NetworkResult
import com.ananjay.noteitdown.utils.TokenManager
import com.ananjay.noteitdown.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

//    private lateinit var binding : FragmentLoginBinding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoginResponse()

        binding.btnLogin.setOnClickListener {
            getUserDataAndLogin()
        }

        //this function (findNavController().popBackStack()) can be used to clear the current fragment from the backstack and open fragment on top of backstack
        //and can be maintained in a "No Account? Please register" click listener
    }


    private fun getUserDataAndLogin(){

        //TODO: Try creating a function in viewmodel or a base fragment class which contains this function and returns Pair<Boolean, String>

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            Toast.makeText(requireContext(), "All the fields are mandatory values", Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireContext(), "Please enter valid email address", Toast.LENGTH_SHORT)
                .show()
        }else{
            val userRequest = UserRequest(email, password)
            authViewModel.loginUser(userRequest)
        }

    }

    private fun observeLoginResponse(){
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    showProgressBar(binding.loginProgress, false)
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                is NetworkResult.Error -> {
                    showProgressBar(binding.loginProgress, false)
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showProgressBar(binding.loginProgress, true)
                    binding.loginProgress.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showProgressBar(progressBar: ProgressBar, makeVisible: Boolean){
        //TODO: Create a base Fragment class and move it there
        if(makeVisible){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}