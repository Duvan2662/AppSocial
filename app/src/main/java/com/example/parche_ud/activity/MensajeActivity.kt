package com.example.parche_ud.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.parche_ud.adapter.MensajeAdaptador
import com.example.parche_ud.databinding.ActivityMensajeBinding


import com.example.parche_ud.model.MensajeModelo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class MensajeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMensajeBinding
    private var enviarId:String? = null
    private var chatId:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMensajeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        verificarIdChat()

        binding.imageView4.setOnClickListener {
            if(binding.suMensaje.text!!.isEmpty()){
                Toast.makeText(this, "Por favor Ingrese un mensaje", Toast.LENGTH_SHORT).show()
            }else{
                almacenarDatos(binding.suMensaje.text.toString())
            }
        }
    }




    //Funcion  que me ayuda a verificar si ya existe un chat
    private fun verificarIdChat() {

        val recibirId = intent.getStringExtra("userId")//Recibe el id de quien va recibir el dato
         enviarId = FirebaseAuth.getInstance().currentUser!!.phoneNumber///numero de quien va enviar el mensaje

         chatId = enviarId+recibirId
        val reversaChatId = recibirId + enviarId


        val referenciaChat = FirebaseDatabase.getInstance().getReference("chats")

        referenciaChat.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(chatId!!)){
                    obtenerDatos(chatId)//llama a obtener datos
                }else if(snapshot.hasChild(reversaChatId)){
                    chatId = reversaChatId
                    obtenerDatos(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MensajeActivity, "Algo salio mal ñero", Toast.LENGTH_SHORT).show()
            }

        })

    }


    //Obtiene los datos de un chat específico en la base de datos , convierte esos datos
    // en objetos MensajeModelo y los muestra en un RecyclerView
    private fun obtenerDatos(chatId: String?) {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val lista = arrayListOf<MensajeModelo>()

                    for (show in snapshot.children){
                        lista.add(show.getValue(MensajeModelo::class.java)!!)
                    }
                    binding.recyclerView2.adapter = MensajeAdaptador(this@MensajeActivity,lista)
                    
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MensajeActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }


    //Almacena los mensajes en la base de datos
    private fun almacenarDatos(msg: String) {

        val fecha : String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val hora : String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["mensaje"] = msg
        map["enviarId"] = enviarId!!
        map["Hora"] = hora
        map["Fecha"] = fecha


        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference.child(reference.push().key!!).setValue(map).addOnCompleteListener {
            if(it.isSuccessful){
                binding.suMensaje.text = null
                Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Algo salio mal ñero", Toast.LENGTH_SHORT).show()
            }
        }

    }
}