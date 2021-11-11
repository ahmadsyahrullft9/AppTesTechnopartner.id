package com.example.myapplication.models

sealed class CategoryAndMenu {
    data class CategoryName(val category_name: String) : CategoryAndMenu()
    data class CategoryMenu(val menu: Menu) : CategoryAndMenu()
}