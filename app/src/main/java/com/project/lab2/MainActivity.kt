package com.project.lab2

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.lab2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    private val list: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createRecyclerView()
        updateList()

        binding.btnAdd.setOnClickListener{ addItem() }
        binding.btnCancel.setOnClickListener{ cancelItem() }
        binding.btnSave.setOnClickListener{ saveItem() }
        binding.etNewName.movementMethod = ScrollingMovementMethod()

    }

    private fun createRecyclerView() {
        adapter = MainAdapter(object : MainActionListener {
            override fun renameItem(name: String) {
                with(binding) {
                    etNewName.setText(name)
                    btnAdd.visibility = View.GONE
                    btnSave.visibility = View.VISIBLE
                    rvList.visibility = View.INVISIBLE
                    btnSave.tag = name
                }
            }

            override fun deleteItem(name: String) {
                list.remove(name)
                updateList()
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvList.layoutManager = layoutManager
        binding.rvList.adapter = adapter
    }

    private fun addItem() {
        val name = binding.etNewName.text.toString()
        if (name.isBlank())
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
        else if (name in list.toList())
            Toast.makeText(this, "This name already used", Toast.LENGTH_SHORT).show()
        else {
            list.add(name)
            binding.etNewName.setText("")
            updateList()
        }
    }

    private fun cancelItem() {
        with(binding) {
            etNewName.setText("")
            btnSave.visibility = View.GONE
            btnAdd.visibility = View.VISIBLE
            rvList.visibility = View.VISIBLE
        }
    }

    private fun saveItem() {
        val oldName = binding.btnSave.tag as String
        val newName = binding.etNewName.text.toString()
        if (newName.isBlank())
            Toast.makeText(this, "Name is empty", Toast.LENGTH_SHORT).show()
        else if (oldName == newName)
            cancelItem()
        else if (newName in list.toList())
            Toast.makeText(this, "This name already used", Toast.LENGTH_SHORT).show()
        else {
            val index = list.indexOf(oldName)
            list[index] = newName
            updateList()
            cancelItem()
        }
    }

    private fun updateList() {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }
}