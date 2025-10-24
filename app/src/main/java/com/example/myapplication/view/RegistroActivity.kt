package com.example.myapplication.view

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myapplication.AppDatabase
import com.example.myapplication.R
import com.example.myapplication.dao.UserDao
import kotlinx.coroutines.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_registro)

        // Inicializar Room
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "joyas_db"
        ).build()
        userDao = db.userDao()

        val etNombre = findViewById<EditText>(R.id.et_nombre)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etContrasena = findViewById<EditText>(R.id.et_contrasena)
        val btnSend = findViewById<Button>(R.id.btn_send)

        btnSend.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar a Room en un hilo secundario
                CoroutineScope(Dispatchers.IO).launch {
                    val existente = userDao.findUserByEmail(email)
                    if (existente != null) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegistroActivity, "El correo ya está registrado", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        val nuevoUsuario = UserEntity(name = nombre, email = email, password = contrasena)
                        userDao.registerUser(nuevoUsuario)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RegistroActivity, "Usuario registrado correctamente", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
    }
}