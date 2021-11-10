package com.example.myapplication.fragments

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.lib.BindingFragment
import com.example.myapplication.networks.NetworkState
import com.example.myapplication.viewmodels.LoginViewModel
import com.example.myapplication.viewmodels.LoginViewModelFactory

class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    private lateinit var loginViewModel: LoginViewModel

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun setupView(binding: FragmentLoginBinding) {
        loginViewModel = LoginViewModelFactory(requireContext()).create(LoginViewModel::class.java)
        binding.apply {
            edEmail.addTextChangedListener { edEmail.error = null }
            edPassword.addTextChangedListener { edPassword.error = null }

            btnLogin.setOnClickListener {
                if (TextUtils.isEmpty(edEmail.text)) {
                    edEmail.error = "this field is required"
                    edEmail.requestFocus()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(edPassword.text)) {
                    edPassword.error = "this field is required"
                    edPassword.requestFocus()
                    return@setOnClickListener
                }

                processLogin(edEmail.text.toString(), edPassword.text.toString())
            }
        }

        loginViewModel.apply {
            networkState.observe(this@LoginFragment) { networkState ->
                when (networkState) {
                    NetworkState.LOADING -> showLoading(true)
                    NetworkState.SUCCESS -> showLoading(false)
                    NetworkState.ERROR -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), networkState.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            userAccess.observe(this@LoginFragment) { userAccess ->
                if (userAccess != null) {
                    loginViewModel.updateUserAccessToDataStore(userAccess)
                    Toast.makeText(requireContext(), "login success", Toast.LENGTH_LONG).show()
                    //navigate to home
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    findNavController().popBackStack(R.id.loginFragment, true)
                }
            }
        }
    }

    private fun showLoading(display: Boolean) {

    }

    private fun processLogin(emailAddress: String, password: String) {
        loginViewModel.processLogin(emailAddress, password)
    }

}