package com.example.parche_ud

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.parche_ud.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

//la actividad puede recibir eventos de selección de elementos de un menú de navegación
// ,NavigationView.OnNavigationItemSelectedListener
class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding : ActivityMainBinding
    //variable para el menu tipo amburguesa
    var actionBarDrawerToggle : ActionBarDrawerToggle ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //accesibilidad para abrir y cerrar el menú de navegación.
        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.principal,R.string.Abrir,R.string.Cerrar)
        //escuchará y responderá a eventos en el menú de navegación.
        binding.principal.addDrawerListener(actionBarDrawerToggle!!)
        //animación de transición de la hamburguesa del menú a una flecha de retroceso
        actionBarDrawerToggle!!.syncState()
        //permite que el usuario pueda volver atrás en la navegación de la aplicación.
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)   ->aL LORO SE PUEDE ACTIVAR
        //escuche los eventos de selección de elementos del menú y realice las acciones
        binding.vistaNavegacion.setNavigationItemSelectedListener(this)


        //esta línea de código establece la configuración de la navegación de la aplicación
        // utilizando el componente de navegación de Android Jetpack.
        val controladorNavegacion = findNavController(R.id.fragmento)
        NavigationUI.setupWithNavController(binding.bottomNavigationView,controladorNavegacion)
    }

    //Se ejecuta cuando se seleccione un elemento del menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.universidad ->{
                val url = "https://www.udistrital.edu.co/inicio"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            R.id.compartir ->{
                Toast.makeText(this, "Compartir", Toast.LENGTH_SHORT).show()
            }
            R.id.terminosCondiciones ->{
                val url = "https://docs.google.com/document/d/10iYwf4mhZshGV2M0CwTgQTX3JtjggH4aFbz2Boq6wlI/edit?usp=sharing"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            R.id.facultad ->{
                val url = "http://tecsistematizaciondatos.udistrital.edu.co:8080/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            R.id.desarrollador ->{
                val url = "https://github.com/Duvan2662"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

        }
        return true//Devuelve true para saber que funciono
    }

    //maneje los eventos de la barra de herramientas para la animación de transición de
    // la hamburguesa del menú a una flecha de retroceso y viceversa.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }else{
            super.onOptionsItemSelected(item)
        }
    }

    //permite que el usuario cierre el menú de navegación al presionar el botón de retroceso del dispositivo
    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.principal.isDrawerOpen(GravityCompat.START)){
            binding.principal.close()
        }else{
            super.onBackPressed()
        }
    }
}