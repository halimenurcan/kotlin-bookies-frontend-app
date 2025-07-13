package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.google.android.material.button.MaterialButton

class OtherListAdapter(
    private var lists: List<ListDto>,
    private val onFollowClick: (ListDto) -> Unit,
    private val onBookClick: (Long) -> Unit // listeye tıklanınca
) : RecyclerView.Adapter<OtherListAdapter.OtherListViewHolder>() {

    inner class OtherListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.listTitle)
        private val followButton: MaterialButton = itemView.findViewById(R.id.followButton)
        private val bookContainer: LinearLayout = itemView.findViewById(R.id.bookContainer)

        fun bind(listItem: ListDto) {
            titleView.text = listItem.title

            followButton.setOnClickListener {
                onFollowClick(listItem)
            }

            // Kitapları dinamik olarak ekle
            bookContainer.removeAllViews()

            listItem.books.forEach { book ->
                val bookView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.item_book_grid, bookContainer, false)

                val bookTitle = bookView.findViewById<TextView>(R.id.bookTitle)
                bookTitle.text = book.title

                bookView.setOnClickListener {
                    onBookClick(book.id)
                }

                bookContainer.addView(bookView)
            }

            // See more kartı otomatik XML'de var zaten (include edilmiş)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_other, parent, false)
        return OtherListViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherListViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    fun submitList(newList: List<ListDto>) {
        lists = newList
        notifyDataSetChanged()
    }
}
