package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class FilaCarrito(
    val idProducto: Int,
    val nombre: String,
    val precio: Int,
    val cantidad: Int
) {
    val subtotal: Int get() = precio * cantidad
}

@Dao
interface CarritoDao {

    // UPSERT 1 unidad
    @Query("""
        INSERT INTO carrito(idProducto, cantidad)
        VALUES(:idProducto, 1)
        ON CONFLICT(idProducto) DO UPDATE SET cantidad = cantidad + 1
    """)
    suspend fun agregarUno(idProducto: Int)

    @Query("UPDATE carrito SET cantidad = MAX(cantidad - 1, 0) WHERE idProducto = :idProducto")
    suspend fun restarUno(idProducto: Int)

    @Query("DELETE FROM carrito WHERE idProducto = :idProducto")
    suspend fun eliminarProducto(idProducto: Int)

    @Query("DELETE FROM carrito")
    suspend fun vaciar()

    @Query("""
        SELECT p.id AS idProducto, p.nombre, p.precio, c.cantidad
        FROM carrito c
        JOIN productos p ON p.id = c.idProducto
        ORDER BY p.nombre
    """)
    fun obtenerFilasCarrito(): Flow<List<FilaCarrito>>
}
