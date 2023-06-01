package com.example.parche_ud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parche_ud.activity.MensajeActivity
import com.example.parche_ud.databinding.UsuariosLayoutBinding
import com.example.parche_ud.model.usuariosModelo


//Adaptador que me sirve para ir colocando las imagens y la informacion de los usuarios en el usuarioLayout
class AmistadAdaptador(val context : Context, val lista: ArrayList<usuariosModelo>) :RecyclerView.Adapter<AmistadAdaptador.verAmistades>() {
    inner class verAmistades(val binding:UsuariosLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): verAmistades {
        return verAmistades(UsuariosLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: verAmistades, position: Int) {
        holder.binding.textView5.text = lista[position].nombre //Nombre del usuario en la carta
        holder.binding.textView4.text = lista[position].correo //Correo del usuario en la carta

        Glide.with(context).load(lista[position].imagen).into(holder.binding.usuarioImagen)//Imagen del usuario en la carta


        //Cuando el usuario le da en la imagen del chat
        holder.binding.chat.setOnClickListener {
            val inte = Intent(context, MensajeActivity::class.java)
            inte.putExtra("userId",lista[position].numero)
            context.startActivity(inte)
        }
    }
}