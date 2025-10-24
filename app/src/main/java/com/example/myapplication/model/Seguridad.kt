package com.example.myapplication.model

import java.security.MessageDigest

object Seguridad {
    fun sha256(texto: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(texto.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
