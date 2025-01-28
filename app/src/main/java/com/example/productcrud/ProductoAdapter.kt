package com.example.productcrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.productcrud.models.Producto

class ProductoAdapter(
    private var productos: List<Producto>,
    private val onEditarClick: (Producto) -> Unit,   // callback para Editar
    private val onEliminarClick: (Producto) -> Unit  // callback para Eliminar
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)

        // Aquí asignas la lógica de los botones
        holder.btnEditar.setOnClickListener {
            onEditarClick(producto)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(producto)
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    fun updateData(nuevosProductos: List<Producto>) {
        productos = nuevosProductos
        notifyDataSetChanged()
    }

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        private val tvStock:  TextView = itemView.findViewById(R.id.tvStock)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)

        fun bind(producto: Producto) {
            tvNombre.text = producto.nombre
            tvPrecio.text = "Precio: S/${producto.precio}"
            tvStock.text = "Stock: ${producto.stock}"
        }
    }
}




