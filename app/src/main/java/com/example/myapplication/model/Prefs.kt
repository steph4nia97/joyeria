package com.example.myapplication.model

import android.content.Context

object Prefs {
    private const val ARCHIVO = "PREFS_JOYERIA"
    private const val CLAVE_SESION_CORREO = "sesion_correo"

    fun setSesion(ctx: Context, correo: String) {
        ctx.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
            .edit().putString(CLAVE_SESION_CORREO, correo).apply()
    }

    fun getSesion(ctx: Context): String? =
        ctx.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
            .getString(CLAVE_SESION_CORREO, null)

    fun cerrarSesion(ctx: Context) {
        ctx.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)
            .edit().remove(CLAVE_SESION_CORREO).apply()
    }
}
