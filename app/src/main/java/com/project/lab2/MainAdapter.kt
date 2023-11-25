package com.project.lab2

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.lab2.databinding.ItemBinding

class DiffCallback(
    private val oldList: List<String>,
    private val newList: List<String>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

interface MainActionListener {

    fun renameItem(name: String)

    fun deleteItem(name: String)

}

class MainAdapter(private val actionListener: MainActionListener) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>(),
    View.OnClickListener {
    var items: List<String> = emptyList()
        set(newValue) {
            val diffCallback = DiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View?) {
        val item = v?.tag as String
        when (v.id) {
            R.id.btnRename -> {
                actionListener.renameItem(item)
            }

            R.id.btnDelete -> {
                actionListener.deleteItem(item)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)

        binding.btnRename.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            tvName.text = item
            tvName.movementMethod = ScrollingMovementMethod()
            btnRename.tag = item
            btnDelete.tag = item
        }
    }

    class MainViewHolder(
        val binding: ItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}