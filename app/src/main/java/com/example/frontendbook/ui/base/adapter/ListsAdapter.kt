package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.ui.addBook.AddBookToListBottomSheet
import com.google.android.material.button.MaterialButton

class ListAdapter(
    private var lists: List<ListDto>,
    private val onDeleteClick: (Long) -> Unit,
    private val onClick: (ListDto) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.listTitle)
        private val addCardView: View = itemView.findViewById(R.id.addCardView)
        private val deleteButton: MaterialButton = itemView.findViewById(R.id.deleteButton)

        fun bind(listItem: ListDto) {
            titleView.text = listItem.title

            itemView.setOnClickListener(null)
            addCardView.setOnClickListener {
                AddBookToListBottomSheet.newInstance(listItem.id)
                    .show((itemView.context as FragmentActivity).supportFragmentManager, "AddBookToList")
            }

            deleteButton.setOnClickListener {
                onDeleteClick(listItem.id)
            }
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

    fun submitList(newLists: List<ListDto>) {

        val cleanedLists = newLists.map { list ->
            list.copy(
                books = list.books.distinctBy(BookDto::id)
            )
        }.distinctBy { it.id }

        lists = cleanedLists.toList()
        notifyDataSetChanged()
    }
}
