package com.example.parche_ud.utilidades

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.parche_ud.adapter.MensajeUsuarioAdaptador
import com.example.parche_ud.databinding.FragmentMensajesBinding
import com.example.parche_ud.utilidades.AmigosFragment.Companion.lista
import com.example.parche_ud.utils.Configuracion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MensajesFragment : Fragment() {
    private lateinit var binding : FragmentMensajesBinding

    //Obtiene los datos de firebase y los pone en una lista para mostrarlos en el
    // reciclerView del adapatdorMensaje donde se van a mostrar  la imagen y el nombre del usuario
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMensajesBinding.inflate(layoutInflater)

        getData()
        return binding.root
    }

    private fun getData() {
        Configuracion.informando(requireContext())

        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = arrayListOf<String>()
                    var newList = arrayListOf<String>()
                    for (data in snapshot.children){
                        if (data.key!!.contains(currentId!!)){
                            list.add(data.key!!.replace(currentId!!,""))
                            newList.add(data.key!!)
                        }
                    }

                    try {
                        binding.reciclerview.adapter =
                            MensajeUsuarioAdaptador(requireContext(), list,newList)
                    }catch (e:Exception){

                    }

                    Configuracion.informando2()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }


}