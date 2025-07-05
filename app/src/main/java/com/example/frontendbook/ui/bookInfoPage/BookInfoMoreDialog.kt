package com.example.frontendbook.ui.bookInfoPage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.frontendbook.R
import com.example.frontendbook.data.model.BookInteractionRequest
import com.example.frontendbook.data.repository.UserRepository
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookInfoMoreDialog : DialogFragment() {

    private var isLiked = false
    private var isRead = false
    private var isInReadList = false

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
        Log.d(TAG, "onViewCreated")

        // 1) Argümanlardan Book'u ve prefs'ten userId'yi al
        val book = arguments?.getParcelable<Book>(ARG_BOOK)
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = prefs.getLong(KEY_USER_ID, -1L)
        Log.d(TAG, "Book=$book  UserId=$userId")

        // 2) Repository'yi oluştur
        val repository = UserRepository(RetrofitClient.userApiService(requireContext()))

        // 3) UI elemanlarını bul
        val likeBtn      = view.findViewById<ImageButton>(R.id.likeButton)
        val readBtn      = view.findViewById<ImageButton>(R.id.readButton)
        val readListBtn  = view.findViewById<ImageButton>(R.id.readListButton)
        val cancelBtn    = view.findViewById<TextView>(R.id.cancelButton)
        val saveBtn      = view.findViewById<Button>(R.id.saveButton)
        val ratingBar    = view.findViewById<RatingBar>(R.id.ratingBar)
        val commentInput = view.findViewById<EditText>(R.id.commentInput)

        // 4) Buton dinleyicileri
        cancelBtn.setOnClickListener { dismiss() }

        likeBtn.setOnClickListener {
            isLiked = !isLiked
            likeBtn.setImageResource(if (isLiked) R.drawable.like_filled else R.drawable.like)
            Log.d(TAG, "isLiked=$isLiked")
        }

        readBtn.setOnClickListener {
            isRead = !isRead
            readBtn.setImageResource(if (isRead) R.drawable.read_filled else R.drawable.read_empty)
            Log.d(TAG, "isRead=$isRead")
        }

        readListBtn.setOnClickListener {
            isInReadList = !isInReadList
            readListBtn.setImageResource(
                if (isInReadList) R.drawable.readlist_filled else R.drawable.readlist_empty
            )
            Log.d(TAG, "isInReadList=$isInReadList")
        }

        saveBtn.setOnClickListener {
            // 5) Ön kontroller
            if (book == null) {
                Toast.makeText(requireContext(), "Kitap bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (userId == -1L) {
                Toast.makeText(requireContext(), "Kullanıcı bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
                dismiss()
                return@setOnClickListener
            }

            // 6) Kullanıcının girdiği yorum ve puanı al
            val comment = commentInput.text.toString().takeIf { it.isNotBlank() }
            val rating  = ratingBar.rating.toInt()  // 0–5 arası integer

            // 7) Ağ çağrısını yap
            lifecycleScope.launch {
                try {
                    val req = BookInteractionRequest(
                        userId     = userId,
                        bookId     = book.id,
                        read       = isRead,
                        liked      = isLiked,
                        inReadList = isInReadList,
                        comment    = comment,
                        rating     = rating
                    )

                } catch (e: HttpException) {
                    val code = e.code()
                    val err  = e.response()?.errorBody()?.string().orEmpty()
                    Log.e(TAG, "HTTP $code: $err")
                    Toast.makeText(requireContext(),
                        "Sunucu hatası $code", Toast.LENGTH_LONG).show()

                } catch (e: Exception) {
                    Log.e(TAG, "Unexpected error", e)
                    Toast.makeText(requireContext(),
                        "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
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
