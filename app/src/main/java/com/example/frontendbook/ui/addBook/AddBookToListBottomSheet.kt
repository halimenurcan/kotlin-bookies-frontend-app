package com.example.frontendbook.ui.addBook

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.BottomSheetAddBookToListBinding
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.data.repository.ReviewsRepository
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.CombinedSearchResult
import com.example.frontendbook.ui.base.adapter.AddBookSearchAdapter
import com.example.frontendbook.ui.search.SearchViewModel
import com.example.frontendbook.ui.search.SearchViewModelFactory
import com.example.frontendbook.data.remote.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import android.view.KeyEvent
import android.view.MotionEvent
import com.example.frontendbook.ui.homePage.ListsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.example.frontendbook.ui.homePage.ListsViewModelFactory


class AddBookToListBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddBookToListBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModelFactory(RetrofitClient.searchApiService(requireContext()))
    }
    private lateinit var adapter: AddBookSearchAdapter
    private val listsViewModel: ListsViewModel by activityViewModels {
        ListsViewModelFactory(requireContext())
    }


    private var selectedBook: Book? = null
    private var listId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listId = arguments?.getLong("listId") ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBookToListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = AddBookSearchAdapter { book ->
            selectedBook = book
            binding.addBookTitle.text = book.title
            binding.searchInput.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.bookPreviewArea.visibility = View.VISIBLE
            binding.selectedBookDetails.visibility = View.VISIBLE
            binding.backButton.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(book.coverImageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.bookPreviewImage)
            binding.bookPreviewTitle.text = book.title
            binding.bookPreviewAuthor.text = book.author
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddBookToListBottomSheet.adapter
            visibility = View.GONE
        }

        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearch = actionId == EditorInfo.IME_ACTION_SEARCH
            val isEnter = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            if (isSearch || isEnter) {
                val q = binding.searchInput.text.toString().trim()
                if (q.isNotEmpty()) {
                    searchViewModel.searchBooksAndUsers(q)
                    hideKeyboard()
                }
                true
            } else false
        }

        val closeIcon = ContextCompat.getDrawable(requireContext(), R.drawable.close)
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val icon = if (!s.isNullOrEmpty()) closeIcon else null
                binding.searchInput.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
            }
        })

        binding.searchInput.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.searchInput.compoundDrawables[2]
                if (drawableEnd != null) {
                    val start = binding.searchInput.width - binding.searchInput.paddingEnd - drawableEnd.intrinsicWidth
                    if (event.x >= start) {
                        binding.searchInput.setText("")
                        binding.recyclerView.visibility = View.GONE
                        binding.bookPreviewArea.visibility = View.GONE
                        binding.selectedBookDetails.visibility = View.GONE
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.backButton.setOnClickListener {
            binding.searchInput.visibility = View.VISIBLE
            binding.searchInput.setText("")
            binding.addBookTitle.text = getString(R.string.add_book)
            binding.recyclerView.visibility = View.GONE
            binding.bookPreviewArea.visibility = View.GONE
            binding.selectedBookDetails.visibility = View.GONE
            binding.backButton.visibility = View.GONE
        }

        searchViewModel.combinedResults.observe(viewLifecycleOwner) { results ->
            val books = results.filterIsInstance<CombinedSearchResult.BookResult>().map { it.book }
            adapter.submitList(books)
            binding.recyclerView.visibility = if (books.isNotEmpty()) View.VISIBLE else View.GONE

            if (books.isEmpty() && searchViewModel.searchStarted.value == true && !binding.searchInput.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.saveBookButton.setOnClickListener {
            val book = selectedBook ?: return@setOnClickListener

            val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)
            if (userId == -1L) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {

                if (listId != -1L) {
                    listsViewModel.addBookToList(listId, book.id) { success ->
                        if (success) {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Book added to list", Toast.LENGTH_SHORT).show()
                            }
                            listsViewModel.refreshLists()
                        } else {
                            if (isAdded) {
                                Toast.makeText(requireContext(), "Failed to add to list", Toast.LENGTH_SHORT).show()
                            }
                        }
                        if (isAdded) dismiss()
                    }
                }
            }
        }


        binding.cancelButton.setOnClickListener {
            binding.searchInput.setText("")
            adapter.submitList(emptyList())
            searchViewModel.clearResults()
            binding.bookPreviewArea.visibility = View.GONE
            binding.selectedBookDetails.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.addBookTitle.text = getString(R.string.add_book)
            binding.backButton.visibility = View.GONE
            selectedBook = null
            hideKeyboard()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                val layoutParams = it.layoutParams
                val displayMetrics = resources.displayMetrics
                layoutParams.height = (displayMetrics.heightPixels * 0.8).toInt()
                it.layoutParams = layoutParams
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }


    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(listId: Long) = AddBookToListBottomSheet().apply {
            arguments = Bundle().apply { putLong("listId", listId) }
        }
    }
}
