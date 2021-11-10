package com.example.myapplication.adapters

import com.example.myapplication.fragments.BannerFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class BannerAdapter(fm: FragmentManager, var bannerList: List<String>) :
    FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = bannerList.size

    override fun getPageTitle(position: Int): CharSequence {
        return "OBJECT ${(position + 1)}"
    }

    override fun getItem(position: Int): Fragment {
        val urlBanner = bannerList[position]
        return BannerFragment.newInstance(urlBanner)
    }
}
