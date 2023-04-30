package com.example.parche_ud.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.parche_ud.MainActivity
import com.example.parche_ud.R
import com.example.parche_ud.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class inicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // método FirebaseAuth.getInstance().currentUser para obtener el usuario actualmente autenticado
        // en Firebase Authentication y se asigna a la variable usuarios.
        val usuarios = FirebaseAuth.getInstance().currentUser

        //Se verifica si la variable usuarios es nula o no. Si es nula, se inicia la actividad LoginActivity.
        // De lo contrario, se inicia la actividad MainActivity.
        Handler(Looper.getMainLooper()).postDelayed({
            if(usuarios==null){
                startActivity(Intent(this, LoginActivity::class.java ))
            }else{
                startActivity(Intent(this, MainActivity::class.java ))
            }
            finish()//se utiliza para finalizar la actividad actual y eliminarla
        },2000)


    }
}