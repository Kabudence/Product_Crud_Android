package com.example.productcrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productcrud.api.RetrofitClient
import com.example.productcrud.models.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddProduct: Button
    private lateinit var recyclerProductos: RecyclerView
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddProduct = findViewById(R.id.btnAddProduct)
        recyclerProductos = findViewById(R.id.recyclerProductos)

        recyclerProductos.layoutManager = LinearLayoutManager(this)

        // Creamos el adapter pasándole callbacks
        adapter = ProductoAdapter(
            productos = emptyList(),
            onEditarClick = { producto ->
                // Acción al hacer clic en “Editar”
                editarProducto(producto)
            },
            onEliminarClick = { producto ->
                // Acción al hacer clic en “Eliminar”
                eliminarProducto(producto)
            }
        )

        recyclerProductos.adapter = adapter

        btnAddProduct.setOnClickListener {
            startActivity(Intent(this, FormProductoActivity::class.java))
        }

        obtenerProductos()
    }

    private fun editarProducto(producto: Producto) {
        val intent = Intent(this, FormProductoActivity::class.java)
        intent.putExtra("id", producto.id)
        intent.putExtra("nombre", producto.nombre)
        intent.putExtra("precio", producto.precio)
        intent.putExtra("stock", producto.stock)
        startActivity(intent)
    }

    private fun eliminarProducto(producto: Producto) {
        // Llamamos a la API para borrar
        RetrofitClient.apiService.eliminarProducto(producto.id!! /* <-- si tu modelo lo tiene */)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity,
                            "Producto eliminado", Toast.LENGTH_SHORT).show()
                        // Refrescamos lista
                        obtenerProductos()
                    } else {
                        Log.e("RETROFIT_ERROR", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("RETROFIT_FAILURE", "${t.message}", t)
                }
            })
    }

    private fun obtenerProductos() {
        RetrofitClient.apiService.obtenerProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val productos = response.body() ?: emptyList()
                    adapter.updateData(productos)
                } else {
                    Log.e("RETROFIT_ERROR", "Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Log.e("RETROFIT_FAILURE", "Mensaje: ${t.message}", t)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Volvemos a traer la lista al volver de otras pantallas
        obtenerProductos()
    }
}
