package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.model.entities.ItemCarritoEntity
import kotlinx.coroutines.flow.Flow

// Fila que se muestra en el Recycler del carrito
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

    // --------- Operaciones base sobre la tabla "carrito" ---------

    // Inserta un ítem; si ya existe (por idProducto único), lo ignora
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(item: ItemCarritoEntity)

    // Suma 1 unidad a un ítem existente
    @Query("UPDATE carrito SET cantidad = cantidad + 1 WHERE idProducto = :idProducto")
    suspend fun sumarUno(idProducto: Int)

    // Resta 1 unidad (no baja de 0)
    @Query("UPDATE carrito SET cantidad = MAX(cantidad - 1, 0) WHERE idProducto = :idProducto")
    suspend fun restarUno(idProducto: Int)

    // Borra un producto del carrito (todas sus unidades)
    @Query("DELETE FROM carrito WHERE idProducto = :idProducto")
    suspend fun eliminarProducto(idProducto: Int)

    // Vacía todo el carrito
    @Query("DELETE FROM carrito")
    suspend fun vaciar()

    // Trae un registro del carrito por idProducto (para upsert)
    @Query("SELECT * FROM carrito WHERE idProducto = :idProducto LIMIT 1")
    suspend fun obtenerPorId(idProducto: Int): ItemCarritoEntity?

    // --------- Atajo de negocio (UPSERT) ---------

    // Si no existe, inserta con cantidad=1; si existe, suma 1
    @Transaction
    suspend fun agregarUno(idProducto: Int) {
        val existente = obtenerPorId(idProducto)
        if (existente == null) {
            insertar(ItemCarritoEntity(idProducto = idProducto, cantidad = 1))
        } else {
            sumarUno(idProducto)
        }
    }

    // --------- Consulta de vista (JOIN con productos) ---------

    @Query("""
        SELECT p.id AS idProducto, p.nombre, p.precio, c.cantidad
        FROM carrito c
        JOIN productos p ON p.id = c.idProducto
        ORDER BY p.nombre
    """)
    fun obtenerFilasCarrito(): Flow<List<FilaCarrito>>
}
