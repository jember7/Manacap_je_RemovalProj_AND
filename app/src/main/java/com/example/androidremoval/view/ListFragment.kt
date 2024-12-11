package com.example.androidremoval.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.androidremoval.R
import com.example.androidremoval.viewModel.ListViewModel

class ListFragment : Fragment() {

    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val listView: ListView = view.findViewById(R.id.listView)



        viewModel.items.observe(viewLifecycleOwner) { items ->
            val adapter = CustomAdapter(requireContext(), items,viewModel)
            listView.adapter = adapter
        }




        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchItems()
    }

}
