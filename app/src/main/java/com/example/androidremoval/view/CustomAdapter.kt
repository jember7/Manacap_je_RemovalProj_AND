package com.example.androidremoval.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.androidremoval.R
import com.example.androidremoval.model.Item
import com.example.androidremoval.viewModel.ListViewModel

class CustomAdapter(context: Context, items: List<Item>, private val viewModel: ListViewModel) :
    ArrayAdapter<Item>(context, R.layout.item_custom, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_custom, parent, false)
            viewHolder = ViewHolder()
            viewHolder.itemName = view.findViewById(R.id.itemName)
            viewHolder.editButton = view.findViewById(R.id.editButton)
            viewHolder.deleteButton = view.findViewById(R.id.deleteButton)

            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = getItem(position)
        viewHolder.itemName?.text = item?.name

        viewHolder.editButton?.setOnClickListener {
            item?.let {
                (context as MainActivity).navigateToEdit(it)
            }
        }

        viewHolder.deleteButton?.setOnClickListener {
            showDeleteConfirmationDialog(item)
        }

        return view
    }
    private fun showDeleteConfirmationDialog(item: Item?) {
        val context = context as? MainActivity ?: return
        AlertDialog.Builder(context)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                item?.id?.let { viewModel.deleteItem(it) }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    private class ViewHolder {
        var itemName: TextView? = null
        var editButton: ImageButton? = null
        var deleteButton: ImageButton? = null
    }
}
