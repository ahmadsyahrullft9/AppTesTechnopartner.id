package com.example.myapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCategorymenuBinding
import com.example.myapplication.models.CategoryAndMenu

class CategoryMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val ERROR_MENU_OPTION = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)


    private val binding = ItemCategorymenuBinding.bind(itemView)

    fun onBind(categoryMenu: CategoryAndMenu.CategoryMenu) {
        binding.apply {
            val menu = categoryMenu.menu

            Glide.with(itemView.context)
                .asBitmap()
                .load(menu.photo)
                .apply(ERROR_MENU_OPTION)
                .into(imgMenu)

            txtTitleMenu.text = menu.name
            txtDescMenu.text = menu.description
            txtPriceMenu.text = "Rp.${menu.price}"
        }
    }
}