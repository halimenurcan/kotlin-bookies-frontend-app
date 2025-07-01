package com.example.frontendbook.ui.addBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.frontendbook.databinding.BottomSheetAddBookBinding
import com.example.frontendbook.ui.base.adapter.AddBookSearchAdapter
import com.example.frontendbook.ui.search.SearchViewModel
import com.example.frontendbook.domain.model.CombinedSearchResult
import com.bumptech.glide.Glide
import com.example.frontendbook.R

class AddBookBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: SearchViewModel by activityViewModels()
    private lateinit var adapter: AddBookSearchAdapter
    private var _binding: BottomSheetAddBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AddBookSearchAdapter { selectedBook ->
            binding.addBookTitle.text = selectedBook.title
            binding.recyclerView.visibility = View.GONE
            binding.bookPreviewArea.visibility = View.VISIBLE
            binding.selectedBookDetails.visibility = View.VISIBLE
            binding.commentInput.setText("")
            binding.ratingBar.rating = 0f

            Glide.with(requireContext())
                .load(selectedBook.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.bookPreviewImage)

            binding.bookPreviewTitle.text = selectedBook.title
            binding.bookPreviewAuthor.text = selectedBook.author
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddBookBottomSheet.adapter
            visibility = View.GONE
        }

        binding.cancelButton.setOnClickListener { dismiss() }

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearch = actionId == EditorInfo.IME_ACTION_SEARCH
            val isEnter = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            if (isSearch || isEnter) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchBooksAndUsers(query)
                    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
                }
                true
            } else false
        }

        viewModel.combinedResults.observe(viewLifecycleOwner) { results ->
            val books = results.filterIsInstance<CombinedSearchResult.BookResult>().map { it.book }

            if (books.isNotEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(books)
            } else {
                binding.recyclerView.visibility = View.GONE
                Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        binding.saveBookButton.setOnClickListener {
            val comment = binding.commentInput.text.toString()
            val rating = binding.ratingBar.rating.toInt()
            Toast.makeText(requireContext(), "Book added! ⭐ $rating", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        var isLiked = false
        binding.likeButton.setOnClickListener {
            isLiked = !isLiked
            if (isLiked) {
                binding.likeButton.setImageResource(R.drawable.like_filled)
                binding.likeButton.setBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.buttonSecondary)
                )
            } else {
                binding.likeButton.setImageResource(R.drawable.like)
                binding.likeButton.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.layoutParams?.height =
            (resources.displayMetrics.heightPixels * 0.85).toInt()
    }
}
