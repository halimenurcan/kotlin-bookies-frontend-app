package com.example.frontendbook.ui.homePage

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentInnerBooksBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.bookInfoPage.BookInfoPageFragment
import com.example.frontendbook.ui.common.ThreeColumnFragment
import com.example.frontendbook.ui.viewmodel.BookViewModel

class InnerBooksFragment : Fragment() {

    private var _binding: FragmentInnerBooksBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInnerBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        viewModel.books.observe(viewLifecycleOwner) { books ->
            Log.d("InnerBooksFragment", "Book count: ${books.size}")
            if (books.isNotEmpty()) {
                inflateBooks(binding.popularBooksContainer, books)
                inflateBooks(binding.exploreBooksContainer, books)
            }
        }

        viewModel.fetchBooks("fiction")
    }

    private fun setupListeners() {
        binding.popularArrow.setOnClickListener {
            openThreeColumnPage("Popular This Week", "popular")
        }

        binding.exploreArrow.setOnClickListener {
            openThreeColumnPage("Explore More", "explore")
        }

        binding.exploreSeeAll.setOnClickListener {
            openThreeColumnPage("Explore More", "explore")
        }
    }

    private fun openThreeColumnPage(title: String, type: String) {
        val fragment = ThreeColumnFragment.newInstance(title, type)
        parentFragmentManager.beginTransaction()
            .replace(R.id.innerFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    @SuppressLint("MissingInflatedId")
    private fun inflateBooks(container: ViewGroup, books: List<Book>) {
        val inflater = LayoutInflater.from(requireContext())
        container.removeAllViews()

        books.forEach { book ->
            val itemView = inflater.inflate(R.layout.item_book_grid, container, false)

            val titleView = itemView.findViewById<TextView>(R.id.bookTitle)
            val imageView = itemView.findViewById<ImageView>(R.id.bookImage)

            titleView.text = book.title

            Glide.with(requireContext())
                .load(book.imageUrl ?: R.drawable.bookk)
                .placeholder(R.drawable.bookk)
                .into(imageView)

            // 👇 Kitaba tıklandığında BookInfoPage'e git
            itemView.setOnClickListener {
                val fragment = BookInfoPageFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("book", book)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.innerFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            container.addView(itemView)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
