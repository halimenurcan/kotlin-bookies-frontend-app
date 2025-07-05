package com.example.frontendbook.ui.bookInfoPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentBookInfoPageBinding
import com.example.frontendbook.domain.model.Book
import com.bumptech.glide.request.target.Target


class BookInfoPageFragment : Fragment() {

    private var _binding: FragmentBookInfoPageBinding? = null
    private val binding get() = _binding!!

    private var book: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = arguments?.getParcelable("book")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookInfoPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        book?.let { book ->
            binding.bookTitle.text = book.title
            binding.bookAuthor.text = book.author
            binding.bookMeta.text = "${book.year}\n${book.pageCount} pages"
            binding.bookDescription.text = book.description

            Glide.with(requireContext())
                .load(book.imageUrl ?: R.drawable.bookk)
                .placeholder(R.drawable.bookk)
                .error(R.drawable.bookk)
                .centerCrop()
                .override(Target.SIZE_ORIGINAL)
                .into(binding.bookCoverImage)


            renderRatingStars(book.rating)

            binding.buttonMore.setOnClickListener {
                val dialog = BookInfoMoreDialog.newInstance(book)
                dialog.show(parentFragmentManager, "BookInfoMoreDialog")
            }
        }
    }

    private fun renderRatingStars(rating: Double) {
        val maxStars = 5
        binding.ratingStars.removeAllViews()
        for (i in 1..maxStars) {
            val star = View.inflate(context, R.layout.item_star, null)
            val imageView = star.findViewById<android.widget.ImageView>(R.id.starIcon)
            imageView.setImageResource(
                if (i <= rating.toInt()) R.drawable.star_rated else R.drawable.star_empty
            )
            binding.ratingStars.addView(star)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
