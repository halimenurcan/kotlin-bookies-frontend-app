package com.example.frontendbook.ui.addBook

import android.content.Context
import android.graphics.Color
import android.os.Bundle
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
import com.example.frontendbook.databinding.BottomSheetAddBookBinding
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.domain.model.CombinedSearchResult
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.adapter.AddBookSearchAdapter
import com.example.frontendbook.ui.search.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class AddBookBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddBookBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var adapter: AddBookSearchAdapter

    private var selectedBook: Book? = null
    private var isLiked = false

    private val likedRepo by lazy {
        LikedBooksRepository(
            RetrofitClient.likedBooksApiService(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter kurulumu
        adapter = AddBookSearchAdapter { book ->
            selectedBook = book
            binding.addBookTitle.text = book.title
            binding.recyclerView.visibility = View.GONE
            binding.bookPreviewArea.visibility = View.VISIBLE
            binding.selectedBookDetails.visibility = View.VISIBLE
            binding.commentInput.setText("")
            binding.ratingBar.rating = 0f

            Glide.with(requireContext())
                .load(book.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.bookPreviewImage)
            binding.bookPreviewTitle.text = book.title
            binding.bookPreviewAuthor.text = book.author
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AddBookBottomSheet.adapter
            visibility = View.GONE
        }

        binding.cancelButton.setOnClickListener { dismiss() }

        // Arama
        binding.searchInput.setOnEditorActionListener { _, actionId, event ->
            val isSearch = actionId == EditorInfo.IME_ACTION_SEARCH
            val isEnter = event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
            if (isSearch || isEnter) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchViewModel.searchBooks(query)
                    hideKeyboard()
                }
                true
            } else false
        }

        // Sonuçları göster
        searchViewModel.combinedResults.observe(viewLifecycleOwner) { results ->
            val books = results
                .filterIsInstance<CombinedSearchResult.BookResult>()
                .map { it.book }
            if (books.isNotEmpty()) {
                adapter.submitList(books)
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.GONE
                Toast.makeText(requireContext(), "No books found", Toast.LENGTH_SHORT).show()
            }
        }

        searchViewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // Kaydet butonu (sadece preview alanı açıkken)
        binding.saveBookButton.setOnClickListener {
            // Burada sadece lokal preview; eğer istersen ek kayıt logic’i koyabilirsin
            dismiss()
        }

        // Beğeni butonu
        binding.likeButton.setOnClickListener {
            val book = selectedBook ?: return@setOnClickListener

            // Toggle UI first
            isLiked = !isLiked
            updateLikeButtonUi(isLiked)

            // Kullanıcı ID’sini al
            val prefs = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)
            if (userId == -1L) return@setOnClickListener

            // Gerçek API çağrısı
            lifecycleScope.launch {
                try {
                    val success = if (isLiked) {
                        likedRepo.likeBook(userId, book.id.toLong())
                    } else {
                        likedRepo.unlikeBook(userId, book.id.toLong())
                    }
                    if (!success) {
                        Toast.makeText(requireContext(), "İşlem başarısız", Toast.LENGTH_SHORT).show()
                        // rollback UI
                        isLiked = !isLiked
                        updateLikeButtonUi(isLiked)
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                    isLiked = !isLiked
                    updateLikeButtonUi(isLiked)
                }
            }
        }
    }

    private fun updateLikeButtonUi(liked: Boolean) {
        if (liked) {
            binding.likeButton.setImageResource(R.drawable.like_filled)
            binding.likeButton.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.buttonSecondary)
            )
        } else {
            binding.likeButton.setImageResource(R.drawable.like)
            binding.likeButton.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.layoutParams?.height =
            (resources.displayMetrics.heightPixels * 0.85).toInt()
    }
}
