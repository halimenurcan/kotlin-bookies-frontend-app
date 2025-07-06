// ReviewsAdapter.kt
package com.example.frontendbook.ui.homePage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.data.model.ReviewDto
import com.example.frontendbook.databinding.ItemReviewBinding

class ReviewsAdapter : ListAdapter<ReviewDto, ReviewsAdapter.ViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: ReviewDto) {

            b.reviewAuthor.text = r.userId.toString()   // veya kullanıcı adını çekiyorsanız ona göre
            b.reviewContent.text  = r.comment
            b.reviewRatingBar.rating  = r.score.toFloat()
            b.reviewTimestamp.text     = r.createdAt
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ReviewDto>() {
            override fun areItemsTheSame(a: ReviewDto, b: ReviewDto) = a.id == b.id
            override fun areContentsTheSame(a: ReviewDto, b: ReviewDto) = a == b
        }
    }
}
