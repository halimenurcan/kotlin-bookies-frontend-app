package com.example.frontendbook.ui.homePage

import android.annotation.SuppressLint
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewsAdapter(
    private val likedRepo: LikedReviewsRepository,
    private val userId: Long,
    private val onClick: ((Book) -> Unit)? = null
) : ListAdapter<ReviewDto, ReviewsAdapter.ViewHolder>(DIFF) {

    private val expandedItems = mutableSetOf<Long>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root) {

        @SuppressLint("SetTextI18n")
        fun bind(r: ReviewDto) {
            // Kitap kapağı
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
                            withContext(Dispatchers.Main) {
                                onClick?.invoke(book)
                            }
                        } catch (e: Exception) {
                            Log.e("ImageClick", "Book fetch failed: ${e.message}")
                        }
                    }
                }
            }

            // Yıldızlar
            b.reviewRatingBar.rating = r.score?.toFloat() ?: 0f

            // Yorum içeriği
            b.reviewContent.text = r.comment

            // Açılma durumu
            if (expandedItems.contains(r.id)) {
                b.reviewContent.maxLines = Int.MAX_VALUE
                b.reviewContent.ellipsize = null
            } else {
                b.reviewContent.maxLines = 2
                b.reviewContent.ellipsize = android.text.TextUtils.TruncateAt.END
            }

            // Yorum genişletme/küçültme
            b.reviewContent.setOnClickListener {
                if (expandedItems.contains(r.id))
                    expandedItems.remove(r.id)
                else
                    expandedItems.add(r.id)
                notifyItemChanged(absoluteAdapterPosition)
            }
            b.reviewBookTitle.text = "Yükleniyor..."

            // Kitap başlığı getir (opsiyonel)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repo = BookRepository(b.root.context)
                    val book = repo.fetchBookById(r.bookId)
                    withContext(Dispatchers.Main) {
                        b.reviewBookTitle.text = book.title
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        b.reviewBookTitle.text = "Bilinmeyen Kitap"
                    }
                }
            }

            // Yorum yazarı
            b.reviewAuthor.text = "– ${r.userName}"

            // Zaman etiketi
            b.reviewTimestamp.text = r.createdAt

            // Like durumu ikon ayarlama
            b.likeButton.setImageResource(
                if (r.isLiked) R.drawable.like_filled else R.drawable.like
            )

            // Like butonu tıklama işlemi
            b.likeButton.setOnClickListener {
                val isNowLiked = !r.isLiked
                r.isLiked = isNowLiked
                notifyItemChanged(absoluteAdapterPosition)

                CoroutineScope(Dispatchers.IO).launch {
                    val result = if (isNowLiked) {
                        likedRepo.like(userId, r.id)
                    } else {
                        likedRepo.unlike(userId, r.id)
                    }
                    withContext(Dispatchers.Main) {
                        if (!result) {
                            // Hata olduysa geri al
                            r.isLiked = !isNowLiked
                            notifyItemChanged(absoluteAdapterPosition)
                            Toast.makeText(
                                b.root.context,
                                "İşlem başarısız oldu!",
                                Toast.LENGTH_SHORT
                            ).show()
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
