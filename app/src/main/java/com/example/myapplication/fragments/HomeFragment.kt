package com.example.myapplication.fragments

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.adapters.BannerAdapter
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.lib.BindingFragment
import com.example.myapplication.models.HomeResponse
import com.example.myapplication.networks.NetworkState
import com.example.myapplication.viewmodels.HomeViewModel
import com.example.myapplication.viewmodels.HomeViewModelFactory
import com.rd.draw.data.Orientation
import com.rd.draw.data.RtlMode
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.myapplication.dialogs.QrCodeDialog
import java.lang.Exception

import java.util.*

class HomeFragment : BindingFragment<FragmentHomeBinding>() {

    private lateinit var homeViewModel: HomeViewModel

    private var homeResponse: HomeResponse? = null
    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    private var currentPage = 0
    private var timer: Timer? = null
    private val DELAY_MS: Long = 2000
    private val PERIOD_MS: Long = 2000

    private val TAG = "HomeFragment"

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun setupView(binding: FragmentHomeBinding) {
        homeViewModel = HomeViewModelFactory(requireContext()).create(HomeViewModel::class.java)
        fragmentHomeBinding = binding

        binding.apply {
            homeViewModel.networkState.observe(requireActivity()) {
                when (it) {
                    NetworkState.LOADING -> showLoading(true)
                    NetworkState.SUCCESS -> showLoading(false)
                    NetworkState.ERROR -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            homeViewModel.homeResponse.observe(requireActivity()) {
                homeResponse = it
                if (homeResponse != null && homeResponse?.status == "success") {
                    txtGreeting.text = homeResponse!!.result.greeting
                    txtFullname.text = homeResponse!!.result.name
                    txtSaldo.text = "Rp.${homeResponse!!.result.saldo}"
                    txtPoin.text = homeResponse!!.result.point.toString()

                    setUpBanner()
                } else {
                    showErrorPage()
                }
            }
            cardQrcode.setOnClickListener {
                //show QR CODE dialog
                if (homeResponse != null) showQrCodePage()
                else Toast.makeText(requireContext(), "data for qr code is null", Toast.LENGTH_LONG)
                    .show()
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                homeViewModel.cancelAll()
                homeViewModel.provideUserAccess()
                homeViewModel.homeData()
            }
        }

        homeViewModel.provideUserAccess()
        homeViewModel.homeData()
    }

    private fun setUpBanner() {
        fragmentHomeBinding.apply {
            if (homeResponse!!.result.banner.size < 1) {
                viewSlider.visibility = View.GONE
            } else {
                val bannerAdapter =
                    BannerAdapter(childFragmentManager, homeResponse!!.result.banner)
                viewPagerBanner.adapter = bannerAdapter
                indicator.setViewPager(viewPagerBanner)
                indicator.setOrientation(Orientation.HORIZONTAL)
                indicator.setRtlMode(RtlMode.On)
                indicator.setInteractiveAnimation(false)
                indicator.setAutoVisibility(true)
                indicator.setFadeOnIdle(false)
                viewPagerBanner.setOffscreenPageLimit(bannerAdapter.getCount())

                putar(viewPagerBanner, bannerAdapter)

                viewSlider.visibility = View.VISIBLE
            }
        }
    }

    private fun putar(viewPagerBanner: ViewPager, viewPagerAdapter: BannerAdapter) {
        val NUM_PAGES = viewPagerAdapter.count
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            viewPagerBanner.setCurrentItem(currentPage++, true)
        }
        timer = Timer() // This will create a new Thread
        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
        viewPagerBanner.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                currentPage = position
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun showQrCodePage() {
        val qrCodeFragment = QrCodeDialog(requireContext(), homeResponse!!.result.qrcode)
        qrCodeFragment.show()
    }

    private fun showErrorPage() {

    }

    private fun showLoading(display: Boolean) {
        try {
            fragmentHomeBinding.swipeRefresh.isRefreshing = display
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}