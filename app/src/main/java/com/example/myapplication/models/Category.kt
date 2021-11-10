package com.example.myapplication.models

data class Category(
    val category_name: String,
    val menu: List<Menu>
)

sealed class CategoryAndMenu {
    data class CategoryName(val category_name: String) : CategoryAndMenu()
    data class CategoryMenu(val menu: Menu) : CategoryAndMenu()
}