// ReviewsAdapter.kt
package com.example.frontendbook.ui.homePage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.databinding.ItemReviewBinding

class ReviewsAdapter : ListAdapter<ReviewDto, ReviewsAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val b: ItemReviewBinding)
        : RecyclerView.ViewHolder(b.root) {

        @SuppressLint("SetTextI18n")
        fun bind(r: ReviewDto) {
            // 1) load cover
            Glide.with(b.reviewBookCover.context)
                .load(r.bookCoverUrl)
                .placeholder(R.drawable.placeholder)
                .into(b.reviewBookCover)

            // 2) stars
            b.reviewRatingBar.rating = r.score?.toFloat() ?: 0f

            // 3) content
            b.reviewContent.text = r.comment

            // 4) author
            b.reviewAuthor.text = "– ${r.userName}"

            // 5) timestamp (you can format this with a RelativeTime util if you like)
            b.reviewTimestamp.text = r.createdAt
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ReviewDto>() {
            override fun areItemsTheSame(a: ReviewDto, b: ReviewDto) = a.id == b.id
            override fun areContentsTheSame(a: ReviewDto, b: ReviewDto) = a == b
        }
    }
}
