package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.lib.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>() {

    private val TAG = "MainActivity"

    private lateinit var navController: NavController
    private var prevDestination: NavDestination? = null

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setupView() {
        val slideUp: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val slideDown: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.apply {
            navController.addOnDestinationChangedListener { controller, destination, _ ->
                Log.d(TAG, "setupView: prevDestination = ${prevDestination?.label}")
                when (destination.label) {
                    getString(R.string.fragment_splashscreen),
                    getString(R.string.fragment_login) -> {
                        bottomNavigationView.apply {
                            visibility = View.GONE
                            if (prevDestination != null && prevDestination!!.label == getString(R.string.fragment_menu)) {
                                startAnimation(slideDown)
                                this.menu.findItem(R.id.action_home).isChecked = false
                                this.menu.findItem(R.id.action_menu).isChecked = true
                            }
                        }
                    }
                    getString(R.string.fragment_home),
                    getString(R.string.fragment_menu) -> {
                        bottomNavigationView.apply {
                            visibility = View.VISIBLE
                            if (prevDestination != null &&
                                (prevDestination!!.label == getString(R.string.fragment_splashscreen) ||
                                        prevDestination!!.label == getString(R.string.fragment_login))
                            ) startAnimation(slideUp)
                        }
                    }
                }
                prevDestination = destination
            }
            bottomNavigationView.setOnItemSelectedListener { item ->
                if (prevDestination != null) navController.popBackStack(prevDestination!!.id, true)
                when (item.itemId) {
                    R.id.action_home -> {
                        navController.navigate(R.id.homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.action_menu -> {
                        navController.navigate(R.id.menuFragment)
                        return@setOnItemSelectedListener true
                    }
                    else -> return@setOnItemSelectedListener false
                }
            }
        }
    }
}