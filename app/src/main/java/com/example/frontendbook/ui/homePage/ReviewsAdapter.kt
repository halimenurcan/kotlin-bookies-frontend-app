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
import kotlinx.coroutines.*

class ReviewsAdapter(
    private val likedRepo: LikedReviewsRepository,
    private val userId: Long,
    private var likedReviewIds: List<Long> = emptyList(),
    private val onClick: ((Book) -> Unit)? = null,
    private val onLikedChanged: (() -> Unit)? = null
) : ListAdapter<ReviewDto, ReviewsAdapter.ViewHolder>(DIFF) {

    private val expandedItems = mutableSetOf<Long>()

    // Liked ID listesi fragment tarafından güncellenince:
    fun updateLikedReviewIds(newIds: List<Long>) {
        likedReviewIds = newIds
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = getItem(position)
        // KENDİ LOCAL beğeni listesine göre görseli anında güncelle
        val isLiked = likedReviewIds.contains(review.id)
        holder.bind(review, isLiked)
    }

    inner class ViewHolder(private val b: ItemReviewBinding) : RecyclerView.ViewHolder(b.root) {
        private var lastReviewId: Long? = null
        private var checkLikeJob: Job? = null

        @SuppressLint("SetTextI18n")
        fun bind(r: ReviewDto, isLiked: Boolean) {
            lastReviewId = r.id

            // --- Kitap kapağı ---
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

            // --- Review içeriği & Expand/Kısalt ---
            b.reviewContent.text = r.comment
            if (expandedItems.contains(r.id)) {
                b.reviewContent.maxLines = Int.MAX_VALUE
                b.reviewContent.ellipsize = null
            } else {
                b.reviewContent.maxLines = 2
                b.reviewContent.ellipsize = android.text.TextUtils.TruncateAt.END
            }
            b.reviewContent.setOnClickListener {
                if (expandedItems.contains(r.id))
                    expandedItems.remove(r.id)
                else
                    expandedItems.add(r.id)
                notifyItemChanged(absoluteAdapterPosition)
            }

            // --- Kitap adı async çekiliyor ---
            b.reviewBookTitle.text = "Yükleniyor..."
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

            b.reviewAuthor.text = "– ${r.userName}"
            b.reviewTimestamp.text = r.createdAt
            b.reviewRatingBar.rating = r.score?.toFloat() ?: 0f

            // --- BEĞENİ BUTONU STATE'i fragment'tan gelen veriye göre belirle ---
            b.likeButton.setImageResource(if (isLiked) R.drawable.like_filled else R.drawable.like)
            r.isLiked = isLiked // local UI state için

            // --- Like İşlemi ---
            b.likeButton.setOnClickListener {
                val isNowLiked = !r.isLiked
                r.isLiked = isNowLiked

                // UI anında güncellenir!
                b.likeButton.setImageResource(
                    if (isNowLiked) R.drawable.like_filled else R.drawable.like
                )

                // API çağrısı yapılır (asenkron)
                CoroutineScope(Dispatchers.IO).launch {
                    val result = if (isNowLiked)
                        likedRepo.like(userId, r.id)
                    else
                        likedRepo.unlike(userId, r.id)

                    withContext(Dispatchers.Main) {
                        if (!result) {
                            // Hata olursa eski haline döndür
                            r.isLiked = !isNowLiked
                            b.likeButton.setImageResource(
                                if (r.isLiked) R.drawable.like_filled else R.drawable.like
                            )
                            Toast.makeText(
                                b.root.context,
                                "İşlem başarısız oldu!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Başarılıysa yukarıya haber ver, oradan fragment likedReviewIds'i tekrar güncelleyebilir!
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
