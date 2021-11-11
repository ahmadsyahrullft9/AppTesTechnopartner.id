package com.example.myapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.MenuAdapter
import com.example.myapplication.databinding.ItemCategorynameBinding
import com.example.myapplication.models.CategoryAndMenu

class CategoryNameViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val binding = ItemCategorynameBinding.bind(itemView)

    fun onBind(categoryName: CategoryAndMenu.CategoryName) {
        binding.txtCategoryName.text = categoryName.category_name
    }
}