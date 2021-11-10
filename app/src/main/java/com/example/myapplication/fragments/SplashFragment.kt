package com.example.myapplication.fragments

import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSplashscreenBinding
import com.example.myapplication.lib.BindingFragment
import com.example.myapplication.models.UserAccess
import com.example.myapplication.viewmodels.LoginViewModel
import com.example.myapplication.viewmodels.LoginViewModelFactory
import kotlin.concurrent.thread

class SplashFragment : BindingFragment<FragmentSplashscreenBinding>() {

    private lateinit var loginViewModel: LoginViewModel
    private val max = 5
    private var count = 0
    private var userAccess: UserAccess? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashscreenBinding
        get() = FragmentSplashscreenBinding::inflate

    override fun setupView(binding: FragmentSplashscreenBinding) {
        loginViewModel = LoginViewModelFactory(requireContext()).create(LoginViewModel::class.java)
        loginViewModel.userAccessLiveData.observe(requireActivity()) {
            this.userAccess = it
        }

        thread {
            try {
                while (count < max) {
                    count += 1
                    Thread.sleep(80)
                }
            } finally {
                requireActivity().runOnUiThread {
                    check()
                }
            }
        }
    }

    private fun check() {
        findNavController().popBackStack(R.id.splashFragment, true)
        if (userAccess == null || (!TextUtils.isEmpty(userAccess?.accessToken) && !TextUtils.isEmpty(
                userAccess?.tokenType
            ))
        ) {
            //navigate to home
            findNavController().navigate(R.id.homeFragment)
        } else {
            //navigate to login
            findNavController().navigate(R.id.loginFragment)
        }
    }

}