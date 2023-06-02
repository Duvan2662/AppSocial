package com.example.parche_ud.auth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.parche_ud.MainActivity
import com.example.parche_ud.R
import com.example.parche_ud.databinding.ActivityRegistroBinding
import com.example.parche_ud.model.usuariosModelo
import com.example.parche_ud.utils.Configuracion
import com.example.parche_ud.utils.Configuracion.informando2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding

    //Identificador de Recursos Uniforme, que se utiliza para identificar de manera
    // única un recurso, como una imagen, un video o un archivo de audio.
    private var imagenUrl : Uri? = null


    //Permite al usuario seleccionar una imagen de la galería para guardarla en la base de datos
    // y mostrarla dentro de la aplicación.
    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.GetContent()){
        imagenUrl = it
        binding.imagenUsuario.setImageURI(imagenUrl)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //La actividad seleccionará imágenes de cualquier tipo de archivo soportado
        binding.imagenUsuario.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        //Boton para enviar la informacion
        binding.guardarInformacion.setOnClickListener {
            validarInformacion()
        }
    }

    //Funcion que valida la informacion
    private fun validarInformacion() {
        //valida los campos de texto
        if(binding.nombreUsuario.text.toString().isEmpty()|| binding.correoUsuario.toString().isEmpty() || binding.sedeUsuario.toString().isEmpty() || imagenUrl==null){
            val mensaje1 = getString(R.string.mensaje5)
            Toast.makeText(this,mensaje1,Toast.LENGTH_SHORT).show()
        }else if(!binding.terminosCondiciones.isChecked){//valida los terminos y condiciones
            val mensaje2 = getString(R.string.mensaje6)
            Toast.makeText(this,mensaje2,Toast.LENGTH_SHORT).show()
        }else{

            subirImagen()
        }
    }

    //Funcion para subir la imagen
    private fun subirImagen() {
        Configuracion.informando(this)

        //Firebase Storage servicio de imagenes de Firebase
        //ubicación donde se guardará la imagen del perfil.
        // El nombre de la carpeta y del archivo donde se almacenará la imagen se especifica en el código.
        var almacenamiento = FirebaseStorage.getInstance().getReference("ImagenesDePerfiles").child(FirebaseAuth.getInstance().currentUser!!.uid).child("perfil.jpg")

        //si funciona la subida de la imagen
        almacenamiento.putFile(imagenUrl!!).addOnSuccessListener {
            almacenamiento.downloadUrl.addOnSuccessListener {
                informacion(it)//Envia la url de la imagen a donde se va a guardar la informacion en realtimeDatabase
            }.addOnFailureListener {
                informando2()
                Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {//Si no funciona
            informando2()
            Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
        }

    }

    //Funcion para subir la informacion a realtime Database
    private fun informacion(imagenUrl: Uri?) {

        //Crea un objeto de la clase usuarioModelo y se le asignan los datos por el usuario
        val usuario = usuariosModelo(
            nombre = binding.nombreUsuario.text.toString(),
            imagen = imagenUrl.toString(),
            correo = binding.correoUsuario.text.toString(),
            sede = binding.sedeUsuario.text.toString(),
            numero = FirebaseAuth.getInstance().currentUser!!.phoneNumber!!
        )
        //Se guarda esta informacion en la tabla usuarios,
        //Cada usuario esta identificado con su numero de telefono
        FirebaseDatabase.getInstance().getReference("usuarios").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).setValue(usuario).addOnCompleteListener{
            informando2()
            //Si  sale correcto inicia la actividad main activity que es donde va estar los demas usuarios
            if(it.isSuccessful){
                startActivity(Intent(this,MainActivity::class.java))
                val mensaje = getString(R.string.mensaje7)
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, it.exception!!.message, Toast.LENGTH_SHORT).show()

            }
        }
    }
}