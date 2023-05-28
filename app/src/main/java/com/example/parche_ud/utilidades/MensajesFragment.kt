package com.example.parche_ud.utilidades

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.parche_ud.adapter.MensajeUsuarioAdaptador
import com.example.parche_ud.databinding.FragmentMensajesBinding
import com.example.parche_ud.utilidades.AmigosFragment.Companion.lista


class MensajesFragment : Fragment() {
    private lateinit var binding : FragmentMensajesBinding

    //Obtiene los datos de firebase y los pone en una lista para mostrarlos en el
    // reciclerView del adapatdorMensaje donde se van a mostrar  la imagen y el nombre del usuario
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMensajesBinding.inflate(layoutInflater)


        binding.reciclerview.adapter = MensajeUsuarioAdaptador(requireContext(), lista!!)
        return binding.root
    }


}