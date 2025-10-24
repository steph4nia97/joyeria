package com.example.myapplication.dao

import androidx.room.*
import com.example.myapplication.model.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre")
    fun obtenerTodos(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<ProductoEntity>)
}
