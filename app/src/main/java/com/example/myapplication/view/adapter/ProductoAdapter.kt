package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.entities.ProductoEntity
import com.google.android.material.button.MaterialButton

class ProductoAdapter(
    private var datos: List<ProductoEntity>,
    private val alAgregar: (ProductoEntity) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.VH>() {

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
    ) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val btnAgregar: MaterialButton = itemView.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(p: ViewGroup, viewType: Int) = VH(p)

    override fun onBindViewHolder(h: VH, pos: Int) {
        val p = datos[pos]
        h.tvNombre.text = p.nombre
        h.tvPrecio.text = "$${p.precio}"
        h.btnAgregar.setOnClickListener { alAgregar(p) }
    }

    override fun getItemCount() = datos.size

    fun actualizar(nuevos: List<ProductoEntity>) {
        datos = nuevos
        notifyDataSetChanged()
    }
}
