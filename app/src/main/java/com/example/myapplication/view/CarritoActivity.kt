package com.example.myapplication.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.BaseDatosJoyeria
import com.example.myapplication.view.adapter.CarritoAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CarritoActivity : ComponentActivity() {

    private val db by lazy { BaseDatosJoyeria.obtenerInstancia(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val rvCarrito = findViewById<RecyclerView>(R.id.rvCarrito)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val btnVaciar = findViewById<MaterialButton>(R.id.btnVaciar)
        val btnFinalizar = findViewById<MaterialButton>(R.id.btnFinalizar)

        rvCarrito.layoutManager = LinearLayoutManager(this)
        val adaptador = CarritoAdapter(
            datos = emptyList(),
            alSumar = { idProd -> lifecycleScope.launch { db.carritoDao().agregarUno(idProd) } },
            alRestar = { idProd -> lifecycleScope.launch { db.carritoDao().restarUno(idProd) } },
            alEliminar = { idProd -> lifecycleScope.launch { db.carritoDao().eliminarProducto(idProd) } }
        )
        rvCarrito.adapter = adaptador

        lifecycleScope.launch {
            db.carritoDao().obtenerFilasCarrito().collectLatest { filas ->
                adaptador.actualizar(filas)
                tvTotal.text = "Total: $${filas.sumOf { it.subtotal }}"
            }
        }

        btnVaciar.setOnClickListener { lifecycleScope.launch { db.carritoDao().vaciar() } }

        btnFinalizar.setOnClickListener {
            lifecycleScope.launch { db.carritoDao().vaciar() }
            finish()
        }
    }
}
