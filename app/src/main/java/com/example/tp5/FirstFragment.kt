package com.example.tp5

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp5.databinding.FragmentFirstBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var personAdapter: PersonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.let { binding ->
            databaseHelper = DatabaseHelper(requireContext())
            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            addButton = binding.floatingActionButton

            // Initialize personAdapter only once
            personAdapter = PersonAdapter(mutableListOf())

            recyclerView.adapter = personAdapter

            addButton.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }

            // Set click listener after initializing personAdapter
            personAdapter.setOnclickItem { selectedItem ->
                val bundle = Bundle().apply {
                    putSerializable("selectedItem", selectedItem)
                }
                findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment, bundle)
            }

            getAllfromDb()
        }
    }

    private fun getAllfromDb() {
        databaseHelper = DatabaseHelper(requireContext())
        val dataList = databaseHelper.getAllDataFromDb()
        Log.d("FirstFragment", "getAllfromDb: Retrieved ${dataList.size} items from the database")
        personAdapter.updateData(dataList)
        personAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}