package com.example.myapplication.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.MenuAdapter
import com.example.myapplication.databinding.FragmentMenuBinding
import com.example.myapplication.dialogs.LoadingDialog
import com.example.myapplication.lib.BindingFragment
import com.example.myapplication.models.Category
import com.example.myapplication.models.CategoryAndMenu
import com.example.myapplication.models.MenuResponse
import com.example.myapplication.networks.NetworkState
import com.example.myapplication.viewmodels.MenuViewModeFactory
import com.example.myapplication.viewmodels.MenuViewModel
import com.google.android.material.tabs.TabLayout
import java.lang.Exception

class MenuFragment : BindingFragment<FragmentMenuBinding>() {

    private lateinit var menuViewModel: MenuViewModel
    private var menuResponse: MenuResponse? = null
    private val categoryAndMenus = ArrayList<CategoryAndMenu>()
    private val categoryAndMenusForTab = ArrayList<CategoryAndMenu.CategoryName>()
    private lateinit var fragmentMenuBinding: FragmentMenuBinding
    private lateinit var loadingDialog: LoadingDialog

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMenuBinding
        get() = FragmentMenuBinding::inflate

    override fun setupView(binding: FragmentMenuBinding) {
        menuViewModel = MenuViewModeFactory(requireContext()).create(MenuViewModel::class.java)
        fragmentMenuBinding = binding
        loadingDialog = LoadingDialog(requireContext())
        binding.apply {
            menuViewModel.networkState.observe(requireActivity()) {
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

            menuViewModel.menuResponse.observe(requireActivity()) {
                menuResponse = it
                if (menuResponse != null)
                    setUpDataMenu()
            }
        }

        menuViewModel.provideUserAccess()
        menuViewModel.menuData()
    }

    private fun setUpDataMenu() {
        categoryAndMenus.clear()
        categoryAndMenusForTab.clear()

        val categories: List<Category> = menuResponse!!.result.categories
        var position = 0;
        categories.forEach { category ->

            val categoryName = CategoryAndMenu.CategoryName(category.category_name, position)
            categoryAndMenus.add(categoryName)
            categoryAndMenusForTab.add(categoryName)

            category.menu.forEach { menu ->
                categoryAndMenus.add(CategoryAndMenu.CategoryMenu(menu))
                position++
            }

            position++
        }

        fragmentMenuBinding.apply {
            val layoutManager = LinearLayoutManager(requireContext())
            val menuAdapter = MenuAdapter(requireContext(), categoryAndMenus)
            rvCategory.setHasFixedSize(true)
            rvCategory.layoutManager = layoutManager
            rvCategory.adapter = menuAdapter

            tabLayout.removeAllTabs()
            categoryAndMenusForTab.forEach { item ->
                val tab = tabLayout.newTab()
                tab.text = item.category_name
                tab.tag = item.position
                tabLayout.addTab(tab)
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val indexOfTab: Int = tab!!.tag as Int
                    layoutManager.scrollToPositionWithOffset(indexOfTab, 0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }
    }

    private fun showLoading(display: Boolean) {
        try {
            loadingDialog.setCancelable(false)
            if (display) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}