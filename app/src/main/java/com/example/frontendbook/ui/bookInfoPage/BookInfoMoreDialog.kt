package com.example.frontendbook.ui.bookInfoPage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.frontendbook.R
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ReviewsRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class BookInfoMoreDialog : DialogFragment() {

    companion object {
        private const val TAG = "BookInfoMoreDialog"
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val ARG_BOOK = "book"

        fun newInstance(book: Book): BookInfoMoreDialog =
            BookInfoMoreDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BOOK, book)
                }
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

        // Kitap ve kullanıcı bilgisi
        val book    = arguments?.getParcelable<Book>(ARG_BOOK)
        val prefs   = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId  = prefs.getLong(KEY_USER_ID, -1L)

        // UI elemanları
        val readBtn     = view.findViewById<ImageView>(R.id.readButton)
        val likeBtn     = view.findViewById<ImageView>(R.id.likeButton)
        val readlistBtn = view.findViewById<ImageView>(R.id.readListButton)
        val commentInput = view.findViewById<EditText>(R.id.commentInput)
        val ratingBar    = view.findViewById<RatingBar>(R.id.ratingBar)
        val saveBtn      = view.findViewById<Button>(R.id.saveButton)
        val cancelBtn    = view.findViewById<TextView>(R.id.cancelButton)

        // Başlangıç ikon durumlarını uygula
        updateReadIcon(readBtn)
        updateLikeIcon(likeBtn)
        updateReadlistIcon(readlistBtn)

        // İptal
        cancelBtn.setOnClickListener { dismiss() }

        // “Okundu” butonu
        readBtn.setOnClickListener {
            // Eğer “Okunacak” seçili ise onu kaldır
            if (isToRead) {
                isToRead = false
                updateReadlistIcon(readlistBtn)
            }
            // Toggle “Okundu”
            isRead = !isRead

            // “Okunmadı” ise like’ı da sıfırla
            if (!isRead && isLiked) {
                isLiked = false
                updateLikeIcon(likeBtn)
            }

            updateReadIcon(readBtn)
        }

        // “Beğen” butonu
        likeBtn.setOnClickListener {
            if (!isRead) {
                Toast.makeText(context, "Önce ‘Okundu’ işaretleyin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            isLiked = !isLiked
            updateLikeIcon(likeBtn)
        }

        // “Okunacak” butonu
        readlistBtn.setOnClickListener {
            // Eğer “Okundu” seçili ise onu ve like’ı sıfırla
            if (isRead) {
                isRead = false
                updateReadIcon(readBtn)
                if (isLiked) {
                    isLiked = false
                    updateLikeIcon(likeBtn)
                }
            }
            // Toggle “Okunacak”
            isToRead = !isToRead
            updateReadlistIcon(readlistBtn)
        }

        // Kaydet butonu: yorum + puan + durumları gönder
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
            if (comment.isEmpty() && rating == 0 && !isRead && !isToRead && !isLiked) {
                Toast.makeText(
                    requireContext(),
                    "En az bir eylem seçin: yorum, puan, okundu/okunacak veya beğeni",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val repo = ReviewsRepository(RetrofitClient.reviewsApiService(requireContext()))
                    val req = ReviewCreateRequest(
                        userId = userId,
                        bookId = book.id,
                        score = rating,
                        comment = comment,
                        read      = isRead,
                        toRead    = isToRead,
                        liked     = isLiked
                    )
                    repo.createComment(req)
                    Toast.makeText(requireContext(), "Kaydedildi!", Toast.LENGTH_SHORT).show()
                    dismiss()
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

    // Yardımcı ikon güncelleme metodları
    private fun updateReadIcon(btn: ImageView) {
        btn.setImageResource(
            if (isRead) R.drawable.read_filled else R.drawable.read_empty
        )
    }
    private fun updateLikeIcon(btn: ImageView) {
        btn.setImageResource(
            if (isLiked) R.drawable.like_filled else R.drawable.like
        )
    }
    private fun updateReadlistIcon(btn: ImageView) {
        btn.setImageResource(
            if (isToRead) R.drawable.readlist_filled else R.drawable.readlist_empty
        )
    }
}
