package com.example.frontendbook.ui.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.api.dto.ListDto
import com.google.android.material.button.MaterialButton

class OtherListAdapter(
    private var lists: List<ListDto>,
    private val onFollowClick: (ListDto) -> Unit,
    private val onBookClick: (BookDto) -> Unit,
    private val onSeeMoreClick: (ListDto) -> Unit
) : RecyclerView.Adapter<OtherListAdapter.OtherListViewHolder>() {

    private var followedListIds: Set<Long> = emptySet()

    fun setFollowedListIds(ids: List<Long>) {
        Log.d("OtherListAdapter", "setFollowedListIds: $ids")
        this.followedListIds = ids.toSet()
        notifyDataSetChanged()
    }

    inner class OtherListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.listTitle)
        private val followButton: MaterialButton = itemView.findViewById(R.id.followButton)
        private val bookContainer: LinearLayout = itemView.findViewById(R.id.bookContainer)

        fun bind(listItem: ListDto) {
            titleView.text = listItem.title

            val isFollowed = followedListIds.contains(listItem.id)
            followButton.text = if (isFollowed) "Unfollow" else "Follow"
            followButton.setBackgroundColor(
                itemView.context.getColor(
                    if (isFollowed) R.color.buttonSecondary else R.color.stars_rated
                )
            )

            followButton.setOnClickListener {
                onFollowClick(listItem)
            }

            bookContainer.removeAllViews()
            val inflater = LayoutInflater.from(itemView.context)

            listItem.books.forEach { book ->
                val bookView = inflater.inflate(R.layout.item_book_grid, bookContainer, false)
                val bookTitle = bookView.findViewById<TextView>(R.id.bookTitle)
                val bookImage = bookView.findViewById<ImageView>(R.id.bookImage)

                bookTitle.text = book.title

                Glide.with(itemView.context)
                    .load(book.coverImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(bookImage)

                bookView.setOnClickListener {
                    onBookClick(book)
                }

                bookContainer.addView(bookView)
            }

            // ✅ See More Card
            val seeMoreCard = inflater.inflate(R.layout.see_more_card, bookContainer, false)
            seeMoreCard.setOnClickListener {
                Log.d("OtherListAdapter", "See more clicked for listId=${listItem.id}")
                onSeeMoreClick(listItem)
            }

            bookContainer.addView(seeMoreCard)
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
