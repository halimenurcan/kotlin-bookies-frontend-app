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

    // ViewHolder: Her kart (list item) görünümünü temsil eder
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.listTitle)

        fun bind(listItem: ListDto) {
            titleView.text = listItem.title
            itemView.setOnClickListener { onClick(listItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_profile, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    // Listeyi dışarıdan güncellemek için çağrılır
    fun submitList(newLists: List<ListDto>) {
        lists = newLists
        notifyDataSetChanged()
    }
}
