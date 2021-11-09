package com.example.learnspanishwithcrys.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.AuthFragmentBinding
import com.example.learnspanishwithcrys.other.Status
import com.example.learnspanishwithcrys.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.auth_fragment) {

    private lateinit var binding: AuthFragmentBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AuthFragmentBinding.bind(view)

        if (viewModel.isLoggedIn()) {
            redirectLogin()
        }


        subscribeToObservers()
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            viewModel.loginUser(email, password)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmedPassword = binding.etRegisterPasswordConfirm.text.toString()
            viewModel.registerUser(email, password, confirmedPassword)
        }



    }
    private fun subscribeToObservers() {

        viewModel.loginStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.loginProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.successful_login)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    redirectLogin()
                }
                Status.ERROR -> {
                    binding.loginProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.unknown_error)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                }
            }
        })


        //
        //
        viewModel.registerStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Status.SUCCESS -> {
                    binding.registerProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.successful_registration)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    binding.registerProgressBar.visibility = View.GONE
                    val message = result.message ?: getString(R.string.unknown_error)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    binding.registerProgressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.matchFragment, true)
            .build()
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToMenuFragment(),
            navOptions
        )
    }
}