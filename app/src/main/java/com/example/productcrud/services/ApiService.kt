package com.example.productcrud.api

import com.example.productcrud.models.Producto
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Obtener todos los productos
    @GET("productos")
    fun obtenerProductos(): Call<List<Producto>>

    // Agregar un producto
    @POST("productos")
    fun agregarProducto(@Body producto: Producto): Call<Producto>

    // Actualizar un producto
    @PUT("productos/{id}")
    fun actualizarProducto(
        @Path("id") id: Int,
        @Body producto: Producto
    ): Call<Producto>

    // Eliminar un producto
    @DELETE("productos/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Void>
}
