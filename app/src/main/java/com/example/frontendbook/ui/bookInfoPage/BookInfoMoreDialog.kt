package com.example.frontendbook.ui.bookInfoPage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.book_info_more_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val book = arguments?.getParcelable<Book>(ARG_BOOK)
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getLong(KEY_USER_ID, -1L)

        // Yorum ve rating UI elemanları
        val commentInput = view.findViewById<EditText>(R.id.commentInput)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val saveBtn = view.findViewById<Button>(R.id.saveButton)
        val cancelBtn = view.findViewById<TextView>(R.id.cancelButton)

        cancelBtn.setOnClickListener { dismiss() }

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
            val rating = ratingBar.rating.toInt()

            if (comment.isEmpty() && rating == 0) {
                Toast.makeText(requireContext(), "Yorum veya puan girin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Yorum gönder!
            lifecycleScope.launch {
                try {
                    val repo = ReviewsRepository(RetrofitClient.reviewsApiService(requireContext()))
                    val req = ReviewCreateRequest(
                        userId = userId,
                        bookId = book.id,
                        score = rating,
                        comment = comment
                    )
                    repo.createComment(req)
                    Toast.makeText(requireContext(), "Yorum kaydedildi!", Toast.LENGTH_SHORT).show()
                    dismiss()
                } catch (e: Exception) {
                    Log.e(TAG, "Review gönderme hatası", e)
                    Toast.makeText(requireContext(), "Yorum kaydedilemedi: ${e.message}", Toast.LENGTH_SHORT).show()
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
}
