package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.ui.addBook.AddBookToListBottomSheet
import com.google.android.material.button.MaterialButton

class ListAdapter(
    private var lists: List<ListDto>,
    private val onClick: (ListDto) -> Unit
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView = itemView.findViewById(R.id.listTitle)
        private val addCardView: View = itemView.findViewById(R.id.addCardView)

        fun bind(listItem: ListDto) {
            titleView.text = listItem.title
            itemView.setOnClickListener { onClick(listItem) }
            addCardView.setOnClickListener {
                AddBookToListBottomSheet.newInstance(listItem.id)
                    .show((itemView.context as FragmentActivity).supportFragmentManager, "AddBookToList")
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
        val listItem = lists[position]
        val followButton = holder.itemView.findViewById<MaterialButton>(R.id.followButton)
        followButton.setOnClickListener {
            val isFollowing = followButton.text == "Follow"

            if (isFollowing) {
                followButton.text = "Following"
                followButton.setBackgroundTintList(
                    ContextCompat.getColorStateList(holder.itemView.context, R.color.buttonSecondary)
                )
            } else {
                followButton.text = "Follow"
                followButton.setBackgroundTintList(
                    ContextCompat.getColorStateList(holder.itemView.context, R.color.stars_rated)
                )
            }
    }
        holder.itemView.setOnClickListener {
            onClick(listItem)
    }
    }

    override fun getItemCount(): Int = lists.size

    // Listeyi dışarıdan güncellemek için çağrılır
    fun submitList(newLists: List<ListDto>) {
        lists = newLists
        notifyDataSetChanged()
    }
}
