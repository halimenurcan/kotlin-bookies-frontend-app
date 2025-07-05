package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.ListDto

class ExploreListAdapter(
    private var lists: List<ListDto>,
    private val onSeeMoreClick: (ListDto) -> Unit  // 👈 Sadece "See More" için tıklama işlemi
) : RecyclerView.Adapter<ExploreListAdapter.ExploreListViewHolder>() {

    inner class ExploreListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.listTitle)
        private val seeMoreCard: View = view.findViewById(R.id.see_more_card)  // 👈 See More kartı ID'si

        fun bind(item: ListDto) {
            titleView.text = item.title

            // Sadece "See More" kartına tıklanınca işlem tetiklenir
            seeMoreCard.setOnClickListener {
                onSeeMoreClick(item)
            }

            // Eğer tüm kart tıklanabilir olsun istersen bunu da ekleyebilirsin:
            // itemView.setOnClickListener { onSeeMoreClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ExploreListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreListViewHolder, position: Int) {
        holder.bind(lists[position])
    }

    override fun getItemCount(): Int = lists.size

    fun submitList(newLists: List<ListDto>) {
        lists = newLists
        notifyDataSetChanged()
    }
}
