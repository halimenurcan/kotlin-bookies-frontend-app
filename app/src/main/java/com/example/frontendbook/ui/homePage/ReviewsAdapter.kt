package com.example.frontendbook.ui.homePage

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.repository.BookRepository
import com.example.frontendbook.data.repository.LikedReviewsRepository
import com.example.frontendbook.databinding.ItemReviewBinding
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.*

class ReviewsAdapter(
    private val likedRepo: LikedReviewsRepository,
    private val userId: Long,
    private var likedReviewIds: MutableList<Long> = mutableListOf(),
    private val onClick: ((Book) -> Unit)? = null,
    private val onLikedChanged: (() -> Unit)? = null
) : ListAdapter<ReviewDto, ReviewsAdapter.ViewHolder>(DIFF) {

    private val expandedItems = mutableSetOf<Long>()

    fun updateLikedReviewIds(newLikedIds: List<Long>) {

        Log.d("ADAPTER_UPDATE", "updateLikedReviewIds called with: $newLikedIds")
        likedReviewIds.clear()
        likedReviewIds.addAll(newLikedIds)

        currentList.forEach { review ->
            review.isLiked = likedReviewIds.contains(review.id)
        }

        submitList(currentList.toList())
    }

    private fun isReviewLiked(reviewId: Long): Boolean {
        return likedReviewIds.contains(reviewId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ViewHolder(private val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root) {

        @SuppressLint("SetTextI18n")
        fun bind(r: ReviewDto) {

            val isLiked = r.isLiked
            Log.d("ADAPTER_BIND", "Binding review id=${r.id}, isLiked=$isLiked")
            Log.d("REVIEW_ADAPTER", "Binding comment: ${r.comment}")

            Glide.with(b.reviewBookCover.context)
                .load(r.bookCoverUrl)
                .placeholder(R.drawable.placeholder)
                .into(b.reviewBookCover)

            b.reviewBookCover.setOnClickListener {
                r.bookId?.let { id ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repo = BookRepository(b.root.context)
                            val book = repo.fetchBookById(id)
                            withContext(Dispatchers.Main) { onClick?.invoke(book) }
                        } catch (e: Exception) {
                            Log.e("ImageClick", "Book fetch failed: ${e.message}")
                        }
                    }
                }
            }

            b.reviewContent.text = r.comment
            b.reviewContent.maxLines = if (expandedItems.contains(r.id)) Int.MAX_VALUE else 2
            b.reviewContent.ellipsize = if (expandedItems.contains(r.id)) null else TextUtils.TruncateAt.END
            b.reviewContent.setOnClickListener {
                if (expandedItems.contains(r.id)) expandedItems.remove(r.id)
                else expandedItems.add(r.id)
                notifyItemChanged(bindingAdapterPosition)
            }

            b.reviewBookTitle.text = "Loading..."
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = BookRepository(b.root.context)
                    val book = repo.fetchBookById(r.bookId)
                    withContext(Dispatchers.Main) {
                        b.reviewBookTitle.text = book.title
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        b.reviewBookTitle.text = "Unknown Book"
                    }
                }
            }

            b.reviewAuthor.text = "– ${r.userName}"
            b.reviewTimestamp.text = r.createdAt
            b.reviewRatingBar.rating = r.score?.toFloat() ?: 0f

            b.likeButton.setImageResource(if (isLiked) R.drawable.like_filled else R.drawable.like)

            b.likeButton.setOnClickListener {
                val isNowLiked = !r.isLiked
                r.isLiked = isNowLiked


                b.likeButton.setImageResource(
                    if (isNowLiked) R.drawable.like_filled else R.drawable.like
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val success = if (isNowLiked)
                        likedRepo.like(userId, r.id)
                    else
                        likedRepo.unlike(userId, r.id)

                    withContext(Dispatchers.Main) {
                        if (!success) {
                            // Geri al
                            r.isLiked = !isNowLiked
                            b.likeButton.setImageResource(
                                if (r.isLiked) R.drawable.like_filled else R.drawable.like
                            )
                            Toast.makeText(b.root.context, "Operation failed!", Toast.LENGTH_SHORT).show()
                        } else {
                            onLikedChanged?.invoke()
                        }
                    }
                }
            }

        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ReviewDto>() {
            override fun areItemsTheSame(a: ReviewDto, b: ReviewDto) = a.id == b.id
            override fun areContentsTheSame(a: ReviewDto, b: ReviewDto) = a == b
        }
    }
}
