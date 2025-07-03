package com.example.frontendbook.ui.homePage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.ListDto

class ListAdapter(
    private var lists: List<ListDto>,
    private val onClick: (ListDto) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.listTitle)

        fun bind(item: ListDto) {
            titleView.text = item.title
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ListViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    fun submitList(newLists: List<ListDto>) {
        lists = newLists
        notifyDataSetChanged()
    }
}
