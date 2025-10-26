package com.example.myapplication.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.dao.CarritoDao
import com.example.myapplication.dao.ProductoDao
import com.example.myapplication.dao.UsuarioDao
import com.example.myapplication.model.entities.ItemCarritoEntity
import com.example.myapplication.model.entities.ProductoEntity
import com.example.myapplication.model.entities.UsuarioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsuarioEntity::class,
        ProductoEntity::class,
        ItemCarritoEntity::class   // 👈 NECESARIO para que exista la tabla "carrito"
    ],
    version = 1,
    exportSchema = false
)
abstract class BaseDatosJoyeria : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao  // 👈 expuesto

    companion object {
        @Volatile private var INSTANCIA: BaseDatosJoyeria? = null

        fun obtenerInstancia(contexto: Context): BaseDatosJoyeria =
            INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosJoyeria::class.java,
                    "joyeria.db"
                )
                    .addCallback(semillaProductos(contexto)) // opcional
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCIA = it }
            }

        private fun semillaProductos(contexto: Context) = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = obtenerInstancia(contexto).productoDao()
                    dao.insertarTodos(
                        listOf(
                            ProductoEntity(1, "Anillo de plata 925", 24990),
                            ProductoEntity(2, "Collar de perlas", 32990),
                            ProductoEntity(3, "Aros de acero inoxidable", 14990),
                            ProductoEntity(4, "Pulsera charm", 21990),
                            ProductoEntity(5, "Anillo oro ajustable", 38990),
                            ProductoEntity(6, "Cadena eslabón grueso", 19990)
                        )
                    )
                }
            }
        }
    }
}
