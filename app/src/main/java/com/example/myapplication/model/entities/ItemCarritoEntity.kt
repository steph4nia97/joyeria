package com.example.myapplication.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "carrito",
    foreignKeys = [
        ForeignKey(
            entity = ProductoEntity::class,
            parentColumns = ["id"],
            childColumns = ["idProducto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idProducto", unique = true)]
)
data class ItemCarritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val idProducto: Int,
    val cantidad: Int = 1
)
