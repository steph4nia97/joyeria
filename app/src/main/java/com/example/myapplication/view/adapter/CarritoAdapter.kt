package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dao.FilaCarrito

class CarritoAdapter(
    private var datos: List<FilaCarrito>,
    private val alSumar: (Int) -> Unit,
    private val alRestar: (Int) -> Unit,
    private val alEliminar: (Int) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.VH>() {

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
    ) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreCarrito)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val btnMas: TextView = itemView.findViewById(R.id.btnMas)
        val btnMenos: TextView = itemView.findViewById(R.id.btnMenos)
        val btnEliminar: TextView = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(p: ViewGroup, viewType: Int) = VH(p)

    override fun onBindViewHolder(h: VH, pos: Int) {
        val r = datos[pos]
        h.tvNombre.text = r.nombre
        h.tvCantidad.text = "x${r.cantidad}"
        h.tvSubtotal.text = "Subtotal: $${r.subtotal}"
        h.btnMas.setOnClickListener { alSumar(r.idProducto) }
        h.btnMenos.setOnClickListener { alRestar(r.idProducto) }
        h.btnEliminar.setOnClickListener { alEliminar(r.idProducto) }
    }

    override fun getItemCount() = datos.size

    fun actualizar(nuevos: List<FilaCarrito>) {
        datos = nuevos
        notifyDataSetChanged()
    }
}
