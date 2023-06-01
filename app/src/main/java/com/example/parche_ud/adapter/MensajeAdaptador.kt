package com.example.parche_ud.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parche_ud.R
import com.example.parche_ud.model.MensajeModelo
import com.example.parche_ud.model.usuariosModelo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


//muestra los mensajes en la interfaz de usuario
class MensajeAdaptador(val context: Context, val lista: List<MensajeModelo>)
    :RecyclerView.Adapter<MensajeAdaptador.MensajeViewHolder>(){

    //Se utilizan para identificar los diferentes tipos de vista
    val derecha = 0
    val izquierda = 1


    //mesaje y foto de cada usuario para el mensaje
    inner class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val texto = itemView.findViewById<TextView>(R.id.mensaje)
        val imagen = itemView.findViewById<ImageView>(R.id.enviarImagen)
    }

    //Dependiendo de si es quien envia o recibe el mensaje la vista se va cambiando
    override fun getItemViewType(position: Int): Int {
        return if (lista[position].enviarId == FirebaseAuth.getInstance().currentUser!!.phoneNumber){
            derecha
        }else izquierda
    }

    //Es quien muestra en los mensajes el lado izquierdo o derecho
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {

        return if(viewType == derecha ){
            MensajeViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_recibir_mensaje,parent,false))
        }else{
            MensajeViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_enviar_mensaje,parent,false))
        }

    }

    //devuelve el número de elementos en la lista de mensajes
    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {

        //Texto del mensaje
        holder.texto.text = lista[position].mensaje


        FirebaseDatabase.getInstance().getReference("usuarios")
            .child(lista[position].enviarId!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){
                            val datos = snapshot.getValue(usuariosModelo::class.java)
                            Glide.with(context).load(datos!!.imagen).placeholder(R.drawable.perfil).into(holder.imagen)//imagen del mesnaje
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }

                })

    }

}