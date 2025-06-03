package com.example.frontendbook.ui.homePage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.databinding.ListItemBinding

class ListsAdapter(private val items: List<String>) :
    RecyclerView.Adapter<ListsAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.listTitle.text = items[position]
        holder.binding.listMore.setOnClickListener {
          //  Log.d("ListsAdapter", "More clicked for: $listName")-liste oka tiklayip more gitmek icin
        }
    }
    override fun getItemCount(): Int = items.size

}
