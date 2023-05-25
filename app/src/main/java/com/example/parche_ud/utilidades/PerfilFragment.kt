package com.example.parche_ud.utilidades

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.parche_ud.R
import com.example.parche_ud.activity.EditarPerfilActivity
import com.example.parche_ud.auth.LoginActivity
import com.example.parche_ud.databinding.FragmentPerfilBinding
import com.example.parche_ud.model.usuariosModelo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class  PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding
    private  lateinit var dialogo : AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        //Dialogo de carga
        binding = FragmentPerfilBinding.inflate(layoutInflater)
        dialogo = AlertDialog.Builder(requireContext()).setView(R.layout.cargar_layout).setCancelable(false).create()
        dialogo.show()

        //Se traen los datos del usuario para colocarlos en el fragmentPerfil
        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get().addOnSuccessListener {
            //Si el usuario existe coloca llena los campos del perfil
            if(it.exists()){
                val datos = it.getValue(usuariosModelo::class.java)
                binding.nombre.setText( datos!!.nombre.toString())
                binding.correo.setText( datos.correo.toString())
                binding.facultad.setText( datos.sede.toString())
                binding.telefono.setText( datos.numero.toString())

                val img = datos.imagen
                Glide.with(requireContext()).load(img).placeholder(R.drawable.perfil).into(binding.imagenUsuario)
                dialogo.dismiss()
            }
        }

        //Boton de cerrar sesion
        binding.salir.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        //Boton de editar perfil
        binding.editarPerfil.setOnClickListener{
            startActivity(Intent(requireContext(), EditarPerfilActivity::class.java))
        }

        return binding.root
    }


}