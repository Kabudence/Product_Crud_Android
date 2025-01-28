package com.example.productcrud.models

data class Producto(
    val nombre: String,
    val precio: Double,
    val stock: Int,
    val id: Int? = null   ,   // <--- El id podría ser nulo

)
