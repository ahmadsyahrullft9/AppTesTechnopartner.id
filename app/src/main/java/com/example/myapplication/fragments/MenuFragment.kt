package com.example.myapplication.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentMenuBinding
import com.example.myapplication.lib.BindingFragment

class MenuFragment : BindingFragment<FragmentMenuBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMenuBinding
        get() = FragmentMenuBinding::inflate

    override fun setupView(binding: FragmentMenuBinding) {

    }
}