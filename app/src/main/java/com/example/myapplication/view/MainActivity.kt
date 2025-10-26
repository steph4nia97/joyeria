package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.BaseDatosJoyeria
import com.example.myapplication.model.entities.ItemCarritoEntity
import com.example.myapplication.view.adapter.ProductoAdapter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy { BaseDatosJoyeria.obtenerInstancia(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usamos layout XML (no Compose)
        setContentView(R.layout.activity_main)

        val rvProductos = findViewById<RecyclerView>(R.id.rvProductos)
        val btnVerCarrito = findViewById<MaterialButton>(R.id.btnVerCarrito)

        rvProductos.layoutManager = LinearLayoutManager(this)

        val adaptador = ProductoAdapter(emptyList()) { producto ->
            lifecycleScope.launch {
                val carritoDao = db.carritoDao()
                val existente = carritoDao.obtenerPorId(producto.id)
                if (existente == null) {
                    carritoDao.insertar(ItemCarritoEntity(idProducto = producto.id, cantidad = 1))
                } else {
                    carritoDao.sumarUno(producto.id)
                }
                Toast.makeText(this@MainActivity, "Añadido: ${producto.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
        rvProductos.adapter = adaptador

        lifecycleScope.launch {
            db.productoDao().obtenerTodos().collectLatest { lista ->
                adaptador.actualizar(lista)
            }
        }

        btnVerCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }
}
