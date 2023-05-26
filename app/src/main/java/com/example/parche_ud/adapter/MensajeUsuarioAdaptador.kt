package com.example.parche_ud.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parche_ud.databinding.ContenedorUsuarioMensajeLayoutBinding
import com.example.parche_ud.model.usuariosModelo

//Adaptador que me sirve para ir colocando las imagens y el nombre en el layout Fragment_mensaje
class MensajeUsuarioAdaptador(val context: Context, val lista:ArrayList<usuariosModelo>) : RecyclerView.Adapter<MensajeUsuarioAdaptador.MensajeUsuarioViewHolder>() {
    inner class MensajeUsuarioViewHolder(val binding: ContenedorUsuarioMensajeLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeUsuarioViewHolder {
        return MensajeUsuarioViewHolder(ContenedorUsuarioMensajeLayoutBinding.inflate(LayoutInflater.from(context), parent,false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: MensajeUsuarioViewHolder, position: Int) {

        Glide.with(context).load(lista[position].imagen).into(holder.binding.imagenUsuario)
        holder.binding.nombreUsuario.text = lista[position].nombre
    }
}