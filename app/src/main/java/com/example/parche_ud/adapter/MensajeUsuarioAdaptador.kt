package com.example.parche_ud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parche_ud.activity.MensajeActivity
import com.example.parche_ud.databinding.ContenedorUsuarioMensajeLayoutBinding
import com.example.parche_ud.model.usuariosModelo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//Adaptador que me sirve para ir colocando las imagens y el nombre en el layout Fragment_mensaje
class MensajeUsuarioAdaptador(val context: Context, val lista:ArrayList<String>,val chatKey:List<String>) : RecyclerView.Adapter<MensajeUsuarioAdaptador.MensajeUsuarioViewHolder>() {
    inner class MensajeUsuarioViewHolder(val binding: ContenedorUsuarioMensajeLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeUsuarioViewHolder {
        return MensajeUsuarioViewHolder(ContenedorUsuarioMensajeLayoutBinding.inflate(LayoutInflater.from(context), parent,false))
    }

    //Lista de usuarios con loq ue habla cada usuario cantidad
    override fun getItemCount(): Int {
        return lista.size
    }

    //Muestra los usuarios con los que habla el usuario
    override fun onBindViewHolder(holder: MensajeUsuarioViewHolder, position: Int) {

        FirebaseDatabase.getInstance().getReference("usuarios")
            .child(lista[position]).addListenerForSingleValueEvent(
                object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if(snapshot.exists()){
                            val data = snapshot.getValue(usuariosModelo::class.java)
                            Glide.with(context).load(data!!.imagen).into(holder.binding.imagenUsuario)//Trae la imagen
                            holder.binding.nombreUsuario.text = data.nombre//Y el nombre
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }

                })

        holder.itemView.setOnClickListener {//Cuando se le da click se muestra el chat de cada usuario al que se le dio click
            val inte = Intent(context,MensajeActivity::class.java)
            inte.putExtra("chat_id",chatKey[position])
            inte.putExtra("userId",lista[position])
            context.startActivity(inte)
        }
    }
}