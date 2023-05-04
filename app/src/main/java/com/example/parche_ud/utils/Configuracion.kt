package com.example.parche_ud.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.parche_ud.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


//Se definen  objeto Configuracion que
// proporciona dos funciones para mostrar y ocultar un diálogo de progreso mientras
// se carga la información en la aplicación.
object Configuracion {
    private var dialogo : AlertDialog? = null
    //Para ir mirando el progreso de la aplicacion
    fun informando(context : Context){
        dialogo = MaterialAlertDialogBuilder(context).setView(R.layout.cargar_layout).setCancelable(false).create()
        dialogo!!.show()
    }

    //Para ocultar el dialogo del progreso de la informacion
    fun informando2(){
        dialogo!!.dismiss()
    }
}