package com.example.frontendbook.ui.bookInfoPage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.frontendbook.R
import com.example.frontendbook.data.model.ReadEntry
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ReadRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch

class BookInfoMoreDialog : DialogFragment() {

    private var isLiked = false
    private var isRead = false
    private var isInReadList = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.book_info_more_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val likeBtn = view.findViewById<ImageButton>(R.id.likeButton)
        val readBtn = view.findViewById<ImageButton>(R.id.readButton)
        val readListBtn = view.findViewById<ImageButton>(R.id.readListButton)
        val cancelBtn = view.findViewById<TextView>(R.id.cancelButton)
        val saveBtn = view.findViewById<Button>(R.id.saveButton)

        val book = arguments?.getParcelable<Book>("book")
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1)

        val api = RetrofitClient.readApiService(requireContext())
        val repository = ReadRepository(api)

        cancelBtn.setOnClickListener { dismiss() }

        likeBtn.setOnClickListener {
            isLiked = !isLiked
            likeBtn.setImageResource(if (isLiked) R.drawable.like_filled else R.drawable.like)
        }

        readBtn.setOnClickListener {
            isRead = !isRead
            readBtn.setImageResource(if (isRead) R.drawable.read_filled else R.drawable.read_empty)
        }

        readListBtn.setOnClickListener {
            isInReadList = !isInReadList
            readListBtn.setImageResource(
                if (isInReadList) R.drawable.readlist_filled else R.drawable.readlist_empty
            )
        }

        saveBtn.setOnClickListener {
            if (book == null || userId == -1L) {
                Toast.makeText(requireContext(), "Kitap veya kullanıcı bilgisi bulunamadı", Toast.LENGTH_SHORT).show()
                dismiss()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    if (isRead) {
                        val entry = ReadEntry(
                            userId = userId,
                            bookId = book.id.toString(),
                            bookTitle = book.title,
                            bookCoverUrl = book.imageUrl ?: ""
                        )
                        repository.addToReadList(entry)
                    }

                    // TODO: Likes ve Readlist sistemleri de buraya benzer şekilde eklenebilir

                    Toast.makeText(requireContext(), "Kaydedildi", Toast.LENGTH_SHORT).show()
                    dismiss()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
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

    companion object {
        fun newInstance(book: Book): BookInfoMoreDialog {
            val fragment = BookInfoMoreDialog()
            val args = Bundle().apply {
                putParcelable("book", book)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
