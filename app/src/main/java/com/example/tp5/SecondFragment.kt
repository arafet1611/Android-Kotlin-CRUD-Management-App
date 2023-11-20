package com.example.tp5

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tp5.databinding.FragmentSecondBinding
import kotlin.random.Random

class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        binding.fab.setOnClickListener {
            addPerson()
        }
    }

    private fun addPerson() {
        val name = binding.editTextName.text.toString()
        val telephone = binding.editTextTelephone.text.toString()
        val email = binding.editTextEmail.text.toString()
        val rue = binding.editTextRue.text.toString()
        val ville = binding.editTextVille.text.toString()
        val codePostale = binding.editTextCodePostale.text.toString()

        val myDataModel = MyDataModel(
            id = Random.Default.nextLong(),
            name = name,
            telephone = telephone,
            email = email,
            rue = rue,
            ville = ville,
            codePostale = codePostale
        )

        val status = databaseHelper.insertIntoDb(myDataModel)

        if (status > -1) {
            Toast.makeText(requireContext(), "Person added successfully", Toast.LENGTH_SHORT).show()

            Handler(requireContext().mainLooper).postDelayed({
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }, 1000)
        } else {
            Toast.makeText(requireContext(), "Error adding person", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        databaseHelper.close()
    }
}

