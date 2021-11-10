package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentBannerBinding
import com.example.myapplication.lib.BindingFragment


class BannerFragment : BindingFragment<FragmentBannerBinding>() {

    private var imgBanner: ImageView? = null
    private var url: String? = null

    val ERROR_BANNER_OPTION = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)

    companion object {
        fun newInstance(url: String?): BannerFragment {
            val args = Bundle()
            args.putString("url", url)
            val fragment = BannerFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBannerBinding
        get() = FragmentBannerBinding::inflate

    override fun setupView(binding: FragmentBannerBinding) {
        url = getArguments()?.getString("url")
        Glide.with(requireActivity())
            .asBitmap()
            .load(url)
            .apply(ERROR_BANNER_OPTION)
            .into(binding.imgBanner)
    }
}