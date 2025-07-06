package com.example.frontendbook.ui.bookInfoPage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.frontendbook.R
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.model.StatusBook
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ReviewsRepository
import com.example.frontendbook.data.repository.StatusRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class BookInfoMoreDialog : DialogFragment() {

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

        val readBtn     = view.findViewById<ImageView>(R.id.readButton)
        val likeBtn     = view.findViewById<ImageView>(R.id.likeButton)
        val readlistBtn = view.findViewById<ImageView>(R.id.readListButton)
        val commentInput = view.findViewById<EditText>(R.id.commentInput)
        val ratingBar    = view.findViewById<RatingBar>(R.id.ratingBar)
        val saveBtn      = view.findViewById<Button>(R.id.saveButton)
        val cancelBtn    = view.findViewById<TextView>(R.id.cancelButton)

        updateReadIcon(readBtn)
        updateLikeIcon(likeBtn)
        updateReadlistIcon(readlistBtn)

        cancelBtn.setOnClickListener { dismiss() }

        readBtn.setOnClickListener {
            if (isToRead) {
                isToRead = false
                updateReadlistIcon(readlistBtn)
            }
            isRead = !isRead
            if (!isRead && isLiked) {
                isLiked = false
                updateLikeIcon(likeBtn)
            }
            updateReadIcon(readBtn)
        }

        likeBtn.setOnClickListener {
            if (!isRead) {
                Toast.makeText(context, "Önce ‘Okundu’ işaretleyin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            isLiked = !isLiked
            updateLikeIcon(likeBtn)
        }

        readlistBtn.setOnClickListener {
            if (isRead) {
                isRead = false
                updateReadIcon(readBtn)
                if (isLiked) {
                    isLiked = false
                    updateLikeIcon(likeBtn)
                }
            }
            isToRead = !isToRead
            updateReadlistIcon(readlistBtn)
        }

        saveBtn.setOnClickListener {
            if (book == null) {
                Toast.makeText(requireContext(), "Kitap bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (userId == -1L) {
                Toast.makeText(requireContext(), "Kullanıcı bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
                dismiss()
                return@setOnClickListener
            }

            val comment = commentInput.text.toString().trim()
            val rating  = ratingBar.rating.toInt()

            lifecycleScope.launch {
                try {
                    val statusRepo = StatusRepository(RetrofitClient.statusApiService(requireContext()))
                    val reviewRepo = ReviewsRepository(RetrofitClient.reviewsApiService(requireContext()))

                    // Sadece "okuyacağım" seçili, review yok:
                    if (isToRead && !isRead && comment.isEmpty() && rating == 0 && !isLiked) {
                        statusRepo.addWillReadBook(
                            StatusBook(
                                id = 0L, // veya hiç gönderme (bazı API’lerde gerek yok)
                                bookId = book.id,
                                userId = userId,
                                status = "READ", // veya "WILL_READ"
                                createdAt = "", // boş string gönderilebilir, ya da null (gerekirse)
                                book = null     // YENİ kayıt için GEREK YOK! (backend setler)
                            )
                        )
                        Toast.makeText(requireContext(), "Okuyacaklarına eklendi!", Toast.LENGTH_SHORT).show()
                        dismiss()
                        return@launch
                    }

                    // Sadece "okudum" seçili, review yok:
                    if (isRead && !isToRead && comment.isEmpty() && rating == 0 && !isLiked) {
                        statusRepo.addReadBook(
                            StatusBook(
                                id = 0L, // veya hiç gönderme (bazı API’lerde gerek yok)
                                bookId = book.id,
                                userId = userId,
                                status = "READ", // veya "WILL_READ"
                                createdAt = "", // boş string gönderilebilir, ya da null (gerekirse)
                                book = null     // YENİ kayıt için GEREK YOK! (backend setler)
                            )
                        )
                        Toast.makeText(requireContext(), "Okuduklarına eklendi!", Toast.LENGTH_SHORT).show()
                        dismiss()
                        return@launch
                    }

                    // Yorum veya puan girildiyse review gönder (ve status olarak "okudum" da seçiliyse):
                    if ((comment.isNotEmpty() || rating > 0 || isLiked) && isRead) {
                        reviewRepo.createComment(
                            ReviewCreateRequest(
                                userId = userId,
                                bookId = book.id,
                                score = rating,
                                comment = comment,
                                read = isRead,
                                toRead = isToRead,
                                liked = isLiked
                            )
                        )
                        Toast.makeText(requireContext(), "Yorum kaydedildi!", Toast.LENGTH_SHORT).show()
                        dismiss()
                        return@launch
                    }

                    // Sadece like işaretlendiyse ve "okudum" seçili değilse:
                    if (isLiked && !isRead) {
                        Toast.makeText(requireContext(), "Önce 'Okundu' işaretlemelisin.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Hiçbir şey seçilmediyse:
                    Toast.makeText(requireContext(), "En az bir eylem seçin!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Kaydedilemedi: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.6).toInt()
        )
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
}
