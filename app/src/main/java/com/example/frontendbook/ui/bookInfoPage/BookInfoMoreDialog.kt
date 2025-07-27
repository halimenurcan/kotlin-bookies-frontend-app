package com.example.frontendbook.ui.bookInfoPage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.frontendbook.R
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ReadRepository
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.homePage.ReviewsViewModel
import com.example.frontendbook.ui.homePage.ReviewsViewModelFactory
import com.example.frontendbook.ui.likedbooks.LikedBooksViewModel
import kotlinx.coroutines.launch

class BookInfoMoreDialog : DialogFragment() {
    private lateinit var likedBooksViewModel: LikedBooksViewModel
    private lateinit var readRepository: ReadRepository
    private lateinit var reviewsViewModel: ReviewsViewModel

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val ARG_BOOK = "book"

        fun newInstance(book: Book): BookInfoMoreDialog =
            BookInfoMoreDialog().apply {
                arguments = Bundle().apply { putParcelable(ARG_BOOK, book) }
            }
    }

    private var isRead    = false
    private var isToRead  = false
    private var isLiked   = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.book_info_more_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val book    = arguments?.getParcelable<Book>(ARG_BOOK)
        val prefs   = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId  = prefs.getLong(KEY_USER_ID, -1L)


        reviewsViewModel = ViewModelProvider(
            requireActivity(),
            ReviewsViewModelFactory(requireContext())
        )[ReviewsViewModel::class.java]

        readRepository = ReadRepository(RetrofitClient.readApiService(requireContext()))


        val likedRepo = LikedBooksRepository(RetrofitClient.likedBooksApiService(requireContext()))
        likedBooksViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return LikedBooksViewModel(likedRepo) as T
                }
            }
        )[LikedBooksViewModel::class.java]

        val readBtn     = view.findViewById<ImageView>(R.id.readButton)
        val likeBtn     = view.findViewById<ImageView>(R.id.likeButton)
        val readlistBtn = view.findViewById<ImageView>(R.id.readListButton)
        val commentInput = view.findViewById<EditText>(R.id.commentInput)
        val ratingBar    = view.findViewById<RatingBar>(R.id.ratingBar)
        val saveBtn      = view.findViewById<Button>(R.id.saveButton)
        val cancelBtn    = view.findViewById<TextView>(R.id.cancelButton)

        updateReadIcon(readBtn)
        updateReadlistIcon(readlistBtn)
        lifecycleScope.launch {
            isRead = checkIfBookIsRead(userId, book?.id)
            updateReadIcon(readBtn)
            isToRead = checkIfBookIsInToReadList(userId, book?.id)
            updateReadlistIcon(readlistBtn)
        }
        if (userId != -1L && book != null) {
            likedBooksViewModel.checkLiked(userId, book.id)
        }
        likedBooksViewModel.isLiked.observe(viewLifecycleOwner) { liked ->
            isLiked = liked
            updateLikeIcon(likeBtn)
        }
        likedBooksViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        readBtn.setOnClickListener {
            isRead = !isRead
            if (isRead) {
                if (isToRead) {
                    isToRead = false
                    lifecycleScope.launch {
                        removeFromToRead(book, userId)
                        addToRead(book, userId)
                    }
                    updateReadlistIcon(readlistBtn)
                } else {
                    lifecycleScope.launch { addToRead(book, userId) }
                }
            } else {
                lifecycleScope.launch { removeFromRead(book, userId) }
                if (isLiked && userId != -1L && book != null) {
                    likedBooksViewModel.toggleLike(userId, book.id)
                    isLiked = false
                    updateLikeIcon(likeBtn)
                }
            }
            updateReadIcon(readBtn)
        }

        readlistBtn.setOnClickListener {
            isToRead = !isToRead
            if (isToRead) {
                if (isRead) {
                    isRead = false
                    lifecycleScope.launch {
                        removeFromRead(book, userId)
                        addToToRead(book, userId)
                    }
                    updateReadIcon(readBtn)
                    if (isLiked && userId != -1L && book != null) {
                        likedBooksViewModel.toggleLike(userId, book.id)
                        isLiked = false
                        updateLikeIcon(likeBtn)
                    }
                } else {
                    lifecycleScope.launch { addToToRead(book, userId) }
                }
            } else {
                lifecycleScope.launch { removeFromToRead(book, userId) }
            }
            updateReadlistIcon(readlistBtn)
        }

        likeBtn.setOnClickListener {
            if (isLiked) {
                if (userId != -1L && book != null) {
                    likedBooksViewModel.toggleLike(userId, book.id)
                }
            } else {
                if (!isRead) {
                    Toast.makeText(context, "First, check the “Read” box.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (userId != -1L && book != null) {
                    likedBooksViewModel.toggleLike(userId, book.id)
                }
            }
        }

        saveBtn.setOnClickListener {
            if (book == null) {
                Toast.makeText(requireContext(), "No book information found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (userId == -1L) {
                Toast.makeText(requireContext(), "User information not found", Toast.LENGTH_SHORT).show()
                dismiss()
                return@setOnClickListener
            }

            val comment = commentInput.text.toString().trim()
            val rating  = ratingBar.rating.toInt()

            lifecycleScope.launch {
                try {
                    if ((comment.isNotBlank() || rating > 0)) {
                        val request = ReviewCreateRequest(
                            userId = userId,
                            bookId = book.id,
                            comment = comment,
                            score = rating,
                            read = isRead,
                            toRead = isToRead,
                            liked = isLiked
                        )
                        reviewsViewModel.createComment(request)
                        Toast.makeText(requireContext(), "Comment saved!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                    }
                    dismiss()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Could not be saved: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        cancelBtn.setOnClickListener { dismiss() }
    }

    private suspend fun addToRead(book: Book?, userId: Long) {
        if (book == null) return
        readRepository.addToReadBooks(userId, book.id)
        Toast.makeText(requireContext(), "Added to Read!", Toast.LENGTH_SHORT).show()
    }

    private suspend fun removeFromRead(book: Book?, userId: Long) {
        if (book == null) return
        readRepository.removeFromReadBooks(userId, book.id)
        Toast.makeText(requireContext(), "Removed from Read!", Toast.LENGTH_SHORT).show()
    }

    private suspend fun addToToRead(book: Book?, userId: Long) {
        if (book == null) return
        readRepository.addToReadList(userId, book.id)
        Toast.makeText(requireContext(), "Added to ReadList!", Toast.LENGTH_SHORT).show()
    }

    private suspend fun removeFromToRead(book: Book?, userId: Long) {
        if (book == null) return
        readRepository.removeFromReadList(userId, book.id)
        Toast.makeText(requireContext(), "Removed from ReadList!", Toast.LENGTH_SHORT).show()
    }

    private fun updateReadIcon(btn: ImageView) {
        btn.setImageResource(if (isRead) R.drawable.read_filled else R.drawable.read_empty)
    }
    private fun updateLikeIcon(btn: ImageView) {
        btn.setImageResource(if (isLiked) R.drawable.like_filled else R.drawable.like)
    }
    private fun updateReadlistIcon(btn: ImageView) {
        btn.setImageResource(if (isToRead) R.drawable.readlist_filled else R.drawable.readlist_empty)
    }
    private suspend fun checkIfBookIsRead(userId: Long, bookId: Long?): Boolean {
        if (userId == -1L || bookId == null) return false
        val readList = readRepository.getReadBooks(userId)
        return readList.any { it.bookId != null && it.bookId.toString() == bookId.toString() }
    }
    private suspend fun checkIfBookIsInToReadList(userId: Long, bookId: Long?): Boolean {
        if (userId == -1L || bookId == null) return false
        val toReadList = readRepository.getToReadList(userId)
        return toReadList.any { it.bookId != null && it.bookId.toString() == bookId.toString() }
    }
}
