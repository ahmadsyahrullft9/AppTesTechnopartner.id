package com.example.myapplication.models

sealed class CategoryAndMenu {
    data class CategoryName(val category_name: String, var position: Int = 0) : CategoryAndMenu()
    data class CategoryMenu(val menu: Menu) : CategoryAndMenu()
}