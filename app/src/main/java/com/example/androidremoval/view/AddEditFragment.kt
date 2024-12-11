package com.example.androidremoval.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.androidremoval.R
import com.example.androidremoval.model.Item
import com.example.androidremoval.viewModel.ListViewModel
import java.util.UUID

class AddEditFragment : Fragment() {

    private val viewModel: ListViewModel by activityViewModels()
    private var currentItem: Item? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_edit, container, false)
        val nameField: EditText = view.findViewById(R.id.nameField)
        val saveButton: Button = view.findViewById(R.id.saveButton)

        currentItem = arguments?.getParcelable("item")

        if (currentItem != null) {
            nameField.setText(currentItem?.name ?: "")
        } else {
            Log.w("AddEditFragment", "No item passed to edit, creating new")
        }

        saveButton.setOnClickListener {
            val name = nameField.text.toString()
            if (currentItem == null) {
                val newItem = Item(id = UUID.randomUUID().toString(), name = name)
                viewModel.addItem(newItem)
            } else {
                currentItem?.let { existingItem ->
                    val updatedItem = existingItem.copy(name = name)
                    viewModel.updateItem(updatedItem)
                }
            }
            (requireActivity() as MainActivity).navigateBack()
        }

        return view
    }
}
