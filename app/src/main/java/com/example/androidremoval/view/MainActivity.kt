package com.example.androidremoval.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androidremoval.R
import com.example.androidremoval.model.Item

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ListFragment())
            .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                navigateToAdd()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun navigateToAdd() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AddEditFragment())
            .addToBackStack(null)
            .commit()
    }

    fun navigateToEdit(item: Item) {
        val fragment = AddEditFragment()
        fragment.arguments = Bundle().apply { putParcelable("item", item) }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateBack() {
        supportFragmentManager.popBackStack()
    }
}