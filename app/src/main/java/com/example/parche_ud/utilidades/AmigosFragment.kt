package com.example.parche_ud.utilidades

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.parche_ud.R
import com.example.parche_ud.databinding.FragmentAmigosBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class AmigosFragment : Fragment() {

    private lateinit var binding : FragmentAmigosBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAmigosBinding.inflate(layoutInflater)

        getData()
        return binding.root
    }

    private fun getData() {
        FirebaseDatabase.getInstance().getReference("usuarios").addValueEventListener(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("shub","onDataChange: ${snapshot.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


}