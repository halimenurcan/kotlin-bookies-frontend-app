package com.example.frontendbook.ui.bookInfoPage

    import android.os.Bundle
    import android.view.*
    import android.widget.*
    import androidx.fragment.app.DialogFragment
    import com.example.frontendbook.R
    import com.example.frontendbook.domain.model.Book

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
                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                dismiss()
            }

        }
        override fun onStart() {
            super.onStart()
            dialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (resources.displayMetrics.heightPixels * 0.6).toInt() // %60 ekran yüksekliği
            )
        }
    companion object {
        private const val ARG_BOOK = "book"

        fun newInstance(book: Book): BookInfoMoreDialog {
            val fragment = BookInfoMoreDialog()
            val bundle = Bundle().apply {
                putParcelable(ARG_BOOK, book)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    }
