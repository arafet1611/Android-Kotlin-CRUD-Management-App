package com.example.tp5

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tp5.databinding.FragmentThirdBinding
import java.io.Serializable

class ThirdFragment : Fragment() {
    private lateinit var binding: FragmentThirdBinding
    private var myDataModel: MyDataModel? = null
    private var myUpdatedDataModel: MyDataModel = MyDataModel()
    private lateinit var personAdapter: PersonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        personAdapter = PersonAdapter(mutableListOf())
        arguments?.let {
            val bundle = arguments
            val obj: MyDataModel? = bundle?.getSerializable("selectedItem") as MyDataModel?
            obj?.let {

                binding.editTextName.setText(it.name)
                binding.editTextTelephone.setText(it.telephone)
                binding.editTextEmail.setText(it.email)
                binding.editTextRue.setText(it.rue)
                binding.editTextVille.setText(it.ville)
                binding.editTextCodePostale.setText(it.codePostale)

                myDataModel = it
                myUpdatedDataModel = MyDataModel() // Initialize myUpdatedDataModel here
            }
        }


        binding.fab.setOnClickListener {
            myUpdatedDataModel.id = myDataModel?.id ?: 0
            myUpdatedDataModel!!.name = binding.editTextName.text.toString()
            myUpdatedDataModel!!.email = binding.editTextEmail.text.toString()
            myUpdatedDataModel!!.rue = binding.editTextRue.text.toString()
            myUpdatedDataModel!!.ville = binding.editTextVille.text.toString()
            myUpdatedDataModel!!.codePostale = binding.editTextCodePostale.text.toString()
// complete this and change val to var 
            myDataModel?.let {
                updatePersonInDatabase(myUpdatedDataModel!!)

            }
        }
        binding.fabDelete.setOnClickListener {
            myDataModel?.let {
                deletePersonFromDatabase(it.id)
            }
        }
    }
    private fun deletePersonFromDatabase(id: Long) {
        val databaseHelper = DatabaseHelper(requireContext())
        val rowsAffected = databaseHelper.deletePerson(id)

        if (rowsAffected > 0) {
            Toast.makeText(requireContext(), "Person deleted successfully", Toast.LENGTH_SHORT).show()
            Handler(requireContext().mainLooper).postDelayed({
                findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)

            }, 1000)
        } else {
            Toast.makeText(requireContext(), "Failed to delete person", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updatePersonInDatabase(myDataModel: MyDataModel) {
        val databaseHelper = DatabaseHelper(requireContext())

        val rowsAffected = databaseHelper.updatePerson(myDataModel)

        if (rowsAffected > 0) {
            personAdapter.updateItem(myDataModel)

            Toast.makeText(requireContext(), "Person updated successfully with id ${myDataModel?.id}", Toast.LENGTH_SHORT).show()
            Handler(requireContext().mainLooper).postDelayed({
                findNavController().navigate(R.id.action_ThirdFragment_to_FirstFragment)
            }, 1000)
        } else {
            Toast.makeText(requireContext(), "Failed to update person ${myDataModel?.id}", Toast.LENGTH_SHORT).show()
        }
    }
}
