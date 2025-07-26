package com.example.frontendbook.ui.homePage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ReviewDto

class ReviewsAdapter(
    private var items: List<ReviewDto>,
    private val onClick: (ReviewDto) -> Unit
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ratingBar: RatingBar  = view.findViewById(R.id.reviewRatingBar)
        private val contentTv: TextView   = view.findViewById(R.id.reviewContent)
        private val authorTv: TextView    = view.findViewById(R.id.reviewAuthor)
        private val timeTv: TextView      = view.findViewById(R.id.reviewTimestamp)

        @SuppressLint("SetTextI18n")
        fun bind(item: ReviewDto) {

            ratingBar.rating = item.score?.toFloat()!!


            contentTv.text = item.comment


            authorTv.text = "User ${item.userId}"


            timeTv.text = item.createdAt


            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size


    fun submitList(newItems: List<ReviewDto>) {
        items = newItems
        notifyDataSetChanged()
    }
}
