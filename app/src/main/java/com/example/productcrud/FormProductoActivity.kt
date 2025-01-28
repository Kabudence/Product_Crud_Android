package com.example.productcrud

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.productcrud.api.RetrofitClient
import com.example.productcrud.models.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var btnGuardar: Button

    // Variables para detectar si estamos en modo "editar"
    private var productoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_producto)

        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etStock  = findViewById(R.id.etStock)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Verifica si se han pasado datos en el Intent (ej.: "Editar")
        // (Estos nombres "id", "nombre", "precio", "stock" deben coincidir con los putExtra)
        if (intent.hasExtra("id")) {
            productoId = intent.getIntExtra("id", -1)
            val nombre = intent.getStringExtra("nombre")
            val precio = intent.getDoubleExtra("precio", 0.0)
            val stock  = intent.getIntExtra("stock", 0)

            // Rellena los EditText
            etNombre.setText(nombre)
            etPrecio.setText(precio.toString())
            etStock.setText(stock.toString())
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            val stock  = etStock.text.toString().toIntOrNull()

            if (nombre.isNotEmpty() && precio != null && stock != null) {
                // Construye el objeto Producto (sin ID, o con ID nulo)
                // O con ID si tu data class así lo requiere
                val producto = Producto(
                    id = productoId ?: 0,  // asumiendo que si es nuevo, id=0 o ignora
                    nombre = nombre,
                    precio = precio,
                    stock = stock
                )

                if (productoId == null || productoId == -1) {
                    // MODO AGREGAR
                    agregarProducto(producto)
                } else {
                    // MODO EDITAR
                    actualizarProducto(productoId!!, producto)
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun agregarProducto(producto: Producto) {
        RetrofitClient.apiService.agregarProducto(producto).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@FormProductoActivity,
                        "Producto agregado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Cierra y vuelve a MainActivity
                } else {
                    Log.e("RETROFIT_ERROR", "Error code: ${response.code()}")
                    Log.e("RETROFIT_ERROR", "Error body: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@FormProductoActivity,
                        "Error en la respuesta del servidor: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                Log.e("RETROFIT_FAILURE", "Mensaje: ${t.message}", t)
                Toast.makeText(
                    this@FormProductoActivity,
                    "Error en la conexión con el servidor: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun actualizarProducto(id: Int, producto: Producto) {
        RetrofitClient.apiService.actualizarProducto(id, producto).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@FormProductoActivity,
                        "Producto actualizado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Regresa a la lista
                } else {
                    Log.e("RETROFIT_ERROR", "Error code: ${response.code()}")
                    Log.e("RETROFIT_ERROR", "Error body: ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@FormProductoActivity,
                        "Error en la respuesta del servidor: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                Log.e("RETROFIT_FAILURE", "Mensaje: ${t.message}", t)
                Toast.makeText(
                    this@FormProductoActivity,
                    "Error en la conexión con el servidor: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}

