package com.example.parche_ud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.parche_ud.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //esta línea de código establece la configuración de la navegación de la aplicación
        // utilizando el componente de navegación de Android Jetpack.
        val controladorNavegacion = findNavController(R.id.fragmento)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,controladorNavegacion)
    }
}