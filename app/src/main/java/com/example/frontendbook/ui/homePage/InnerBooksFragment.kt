package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentInnerBooksBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.threecolumn.ThreeColumnFragment
import com.example.frontendbook.ui.bookInfoPage.BookInfoPageFragment
import com.example.frontendbook.ui.viewmodel.BookViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.ui.base.adapter.BookAdapter


class InnerBooksFragment : Fragment() {
    private lateinit var exploreAdapter: BookAdapter

    private var _binding: FragmentInnerBooksBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: BookViewModel by viewModels()

    // Arguments
    private var pageTitle: String? = null
    private var type: String? = null

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_TYPE  = "arg_type"

        fun newInstance(title: String, type: String): InnerBooksFragment {
            val fragment = InnerBooksFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_TYPE, type)
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageTitle = it.getString(ARG_TITLE)
            type      = it.getString(ARG_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInnerBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            popularArrow.setOnClickListener {
                openThreeColumnPage("Popular Between Friends", "popular")

            }
            exploreArrow.setOnClickListener {
                openThreeColumnPage("Explore More", "explore")
            }
            exploreSeeAll.setOnClickListener {
                openThreeColumnPage("Explore More", "explore")
            }

            exploreAdapter = BookAdapter { book ->
                (parentFragment as? HomePageFragment)?.openBookDetail(book)
            }

            binding.exploreBooksRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            binding.exploreBooksRecyclerView.adapter = exploreAdapter

            viewModel.books.observe(viewLifecycleOwner) { books ->
                if (books.isNotEmpty()) {
                    inflateBooks(popularBooksContainer, books)
                    exploreAdapter.submitList(books.take(9))
                }
            }
            viewModel.popularBooks.observe(viewLifecycleOwner) { books ->
                if (books.isNotEmpty()) {
                    inflateBooks(binding.popularBooksContainer, books.take(6))
                }
            }
            viewModel.fetchPopularBooks()

            viewModel.fetchBooks(type ?: "fiction")
        }
    }

    private fun openThreeColumnPage(title: String, type: String) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.innerFragmentContainer,
                ThreeColumnFragment.newInstance(title, type)
            )
            .addToBackStack(null)
            .commit()
    }

    private fun inflateBooks(container: ViewGroup, books: List<Book>) {
        container.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        books.forEach { book ->
            val itemView = inflater.inflate(R.layout.item_book_grid, container, false)
            itemView.findViewById<TextView>(R.id.bookTitle).text = book.title
            itemView.findViewById<ImageView>(R.id.bookImage).let { iv ->
                Glide.with(this)
                    .load(book.coverImageUrl ?: R.drawable.bookk)
                    .placeholder(R.drawable.bookk)
                    .into(iv)
            }
            itemView.setOnClickListener {
                (parentFragment as? HomePageFragment)?.openBookDetail(book)
            }
            container.addView(itemView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
