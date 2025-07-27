package com.example.frontendbook.ui.bookInfoPage

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentBookInfoPageBinding
import com.example.frontendbook.domain.model.Book
import com.bumptech.glide.request.target.Target
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ReviewsRepository
import kotlinx.coroutines.launch

class BookInfoPageFragment : Fragment() {

    private var _binding: FragmentBookInfoPageBinding? = null
    private val binding get() = _binding!!
    private val args: BookInfoPageFragmentArgs by navArgs()
    private var book: Book? = null
    private var averageRating: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = args.book
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookInfoPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("BookInfoPage", "Book id: ${book?.id}")
        Log.d("NavController", "Current destination: ${findNavController().currentDestination?.label}")

        book?.let { book ->
            binding.bookTitle.text = book.title
            binding.bookAuthor.text = book.author
            binding.bookMeta.text = "${book.publishedYear}\n${book.pageCount} pages"
            binding.bookDescription.text = book.description

            Glide.with(requireContext())
                .load(book.coverImageUrl ?: R.drawable.bookk)
                .placeholder(R.drawable.bookk)
                .error(R.drawable.bookk)
                .centerCrop()
                .override(Target.SIZE_ORIGINAL)
                .into(binding.bookCoverImage)
            fetchAndRenderAverageRating(book.id)
            book.rating?.let { renderRatingStars(it) }

            binding.buttonMore.setOnClickListener {
                val dialog = BookInfoMoreDialog.newInstance(book)
                dialog.show(parentFragmentManager, "BookInfoMoreDialog")
            }
            binding.bookInforeviewsButton.setOnClickListener {
                val action = BookInfoPageFragmentDirections.actionBookInfoPageToReviewsFragment(book,null)
                findNavController().navigate(action)
            }
        }
        val bookDescription = view.findViewById<TextView>(R.id.bookDescription)
        val readMoreToggle = view.findViewById<TextView>(R.id.readMoreToggle)

        var isExpanded = false

        readMoreToggle.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                bookDescription.maxLines = Integer.MAX_VALUE
                bookDescription.ellipsize = null
                readMoreToggle.text = "Read less"
            } else {
                bookDescription.maxLines = 4
                bookDescription.ellipsize = TextUtils.TruncateAt.END
                readMoreToggle.text = "Read more"
            }
        }

    }

    private fun renderRatingStars(rating: Int) {
        val maxStars = 5
        binding.ratingStars.removeAllViews()
        for (i in 1..maxStars) {
            val star = View.inflate(context, R.layout.item_star, null)
            val imageView = star.findViewById<android.widget.ImageView>(R.id.starIcon)
            imageView.setImageResource(
                when {
                    i <= rating -> R.drawable.star_rated
                    else -> R.drawable.star_empty
                }
            )
            binding.ratingStars.addView(star)
        }
    }
    private fun fetchAndRenderAverageRating(bookId: Long) {
        lifecycleScope.launch {
            try {
                val repo = ReviewsRepository(RetrofitClient.reviewsApiService(requireContext()))
                val reviews: List<ReviewDto> = repo.fetchReviewsForBook(bookId)
                Log.d("BookInfoPage", "Number of reviews received: ${reviews.size}")
                reviews.forEachIndexed { i, r ->
                    Log.d("BookInfoPage", "Review $i: score=${r.score}")
                }
                averageRating = if (reviews.isNotEmpty()) {
                    val totalScore = reviews.sumOf { it.score ?: 0 }
                    Log.d("BookInfoPage", "Total points: $totalScore, Review Count: ${reviews.size}")

                    Math.floor(totalScore.toDouble() / reviews.size).toInt()
                } else {
                    0
                }
                Log.d("BookInfoPage", "Average: $averageRating")
                renderRatingStars(averageRating)
            } catch (e: Exception) {
                Log.e("BookInfoPage", "Average could not be calculated: ${e.message}")
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
