package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.CategoryAndMenu
import com.example.myapplication.viewholders.CategoryMenuViewHolder
import com.example.myapplication.viewholders.CategoryNameViewHolder

class MenuAdapter(
    private val context: Context,
    private var categoryAndMenuList: ArrayList<CategoryAndMenu>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_TYPE = 1
        const val CATEGORY_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        when (viewType) {
            ITEM_TYPE -> {
                itemView =
                    LayoutInflater.from(context).inflate(R.layout.item_categorymenu, parent, false)
                return CategoryMenuViewHolder(itemView)
            }
            else -> { //CATEGORY_TYPE
                itemView =
                    LayoutInflater.from(context).inflate(R.layout.item_categoryname, parent, false)
                return CategoryNameViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryNameViewHolder -> {
                val categoryName: CategoryAndMenu.CategoryName =
                    categoryAndMenuList[position] as CategoryAndMenu.CategoryName
                holder.onBind(categoryName)
            }
            is CategoryMenuViewHolder -> {
                val categoryMenu: CategoryAndMenu.CategoryMenu =
                    categoryAndMenuList[position] as CategoryAndMenu.CategoryMenu
                holder.onBind(categoryMenu)
            }
        }
    }

    override fun getItemCount(): Int = categoryAndMenuList.size

    override fun getItemViewType(position: Int): Int {
        val categoryAndMenu = categoryAndMenuList[position]
        return when (categoryAndMenu) {
            is CategoryAndMenu.CategoryName -> CATEGORY_TYPE
            is CategoryAndMenu.CategoryMenu -> ITEM_TYPE
        }
    }
}