package com.example.parche_ud.utilidades

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.parche_ud.adapter.AmistadAdaptador
import com.example.parche_ud.databinding.FragmentAmigosBinding
import com.example.parche_ud.model.usuariosModelo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction


class AmigosFragment : Fragment() {

    private lateinit var binding : FragmentAmigosBinding
    private lateinit var pendiente : CardStackLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAmigosBinding.inflate(layoutInflater)

        obtenerDatos()
        return binding.root
    }



    //Obtiene los datos de firebase y los pone en una lista para mostrarlos en el
    // reciclerView del adapatdor donde se van a mostar  los datos de los usuarios (Imagen, correo, nombre)
    companion object {
        var lista:ArrayList<usuariosModelo>? = null//Donde se van a guardar todos los usuarios de firebase
    }
    private fun obtenerDatos() {
        FirebaseDatabase.getInstance().getReference("usuarios").addValueEventListener(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("shub","onDataChange: ${snapshot.toString()}")
                if(snapshot.exists()){
                     lista = arrayListOf<usuariosModelo>()
                    for(datos in snapshot.children){
                        val modelo = datos.getValue(usuariosModelo::class.java)
                        lista!!.add(modelo!!)
                    }
                    lista!!.shuffle()//Mezcla aleatoriamente los usuarios
                    iniciar()
                    binding.cartavistas.layoutManager = pendiente
                    binding.cartavistas.itemAnimator = DefaultItemAnimator()//gestionar las animaciones
                    binding.cartavistas.adapter = AmistadAdaptador(requireContext(),lista!!)
                }else{
                    Toast.makeText(requireContext(), "Hubo un error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    //Esta funcion es practicamentre la que le de la la animacion a cada carta
    private fun iniciar() {
        pendiente = CardStackLayoutManager(requireContext(), object : CardStackListener{
            override fun onCardDragging(direction: Direction?, ratio: Float) {

            }

            //Se invoca cuando se realiza un deslizamiento completo de una tarjeta en una dirección específica
            override fun onCardSwiped(direction: Direction?) {
                //verifica si la tarjeta superior en la pila es la última y muestra un mensaje de "última carta
                if(pendiente.topPosition == lista!!.size){
                    Toast.makeText(requireContext(), "Esta es la ultima carta", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCardRewound() {

            }

            override fun onCardCanceled() {

            }

            override fun onCardAppeared(view: View?, position: Int) {

            }

            override fun onCardDisappeared(view: View?, position: Int) {

            }

        })
        //Establece el número de tarjetas visibles simultáneamente en la pila en 3
        pendiente.setVisibleCount(3)
        //Define el valor de la distancia de desplazamiento entre cada tarjeta adyacente en la pila.
        pendiente.setTranslationInterval(0.6f)
        //Define el valor de la escala de tamaño entre cada tarjeta adyacente en la pila.
        pendiente.setScaleInterval(0.8f)
        //Establece el ángulo máximo de rotación en grados para las tarjetas en la pila.
        pendiente.setMaxDegree(20.0f)
        // Establece la dirección en la que se pueden desplazar las tarjetas, en este caso, horizontalmente.
        pendiente.setDirections(Direction.HORIZONTAL)
    }


}