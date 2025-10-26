package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.view.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.BaseDatosJoyeria
import com.example.myapplication.model.Prefs
import com.example.myapplication.model.Seguridad
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val db by lazy { BaseDatosJoyeria.obtenerInstancia(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Si ya hay sesión iniciada, pasa directo al catálogo
        Prefs.getSesion(this)?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnIngresar = findViewById<MaterialButton>(R.id.btnIngresar)
        val btnRegistrar = findViewById<MaterialButton>(R.id.btnIrRegistro)

        btnIngresar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val usuario = db.usuarioDao().buscarPorCorreo(correo)
                val hash = Seguridad.sha256(contrasena)

                if (usuario != null && usuario.contrasena == hash) {
                    Prefs.setSesion(this@LoginActivity, correo)
                    Toast.makeText(this@LoginActivity, "Bienvenida/o, ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}
