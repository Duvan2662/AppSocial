package com.example.parche_ud.model


//Clase para los usuarios
data class usuariosModelo(
    val numero : String? = "",
    val nombre : String? = "",
    val sede : String? = "",
    val correo : String?="",
    val genero : String? = "",
    val buscando : String? = "",
    val gustos : String? = "",
    val signo : String? = "",
    val edad : String? = "",
    val imagen : String? = ""
)
