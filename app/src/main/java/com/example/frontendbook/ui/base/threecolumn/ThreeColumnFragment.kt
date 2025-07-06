package com.example.frontendbook.ui.base.threecolumn

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentThreeColumnBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.adapter.BookAdapter
import com.example.frontendbook.ui.bookInfoPage.BookInfoPageFragment
import com.example.frontendbook.ui.profile.ReadViewModel
import com.example.frontendbook.ui.profile.ReadViewModelFactory
import com.example.frontendbook.ui.viewmodel.BookViewModel

class ThreeColumnFragment : Fragment() {

    private var _binding: FragmentThreeColumnBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var readViewModel: ReadViewModel
    private lateinit var adapter: BookAdapter

    private var pageTitle: String? = null
    private var type: String? = null
    private var listId: Long? = null  // 👈 Yeni: Liste ID'si

    companion object {
        internal const val ARG_TITLE = "arg_title"
        internal const val ARG_TYPE = "arg_type"
        internal const val ARG_LIST_ID = "arg_list_id"
        const val ARG_USER_ID = "arg_user_id"


        fun newInstance( title: String,
                         type: String? = null,
                         listId: Long?  = null,
                         userId: Long?  = null): ThreeColumnFragment {

                return ThreeColumnFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TITLE, title)
                        type?.let  { putString(ARG_TYPE, it) }
                        listId?.let { putLong(ARG_LIST_ID, it) }
                        userId?.let { putLong(ARG_USER_ID, it) }
                    }
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            pageTitle = arguments?.getString(ARG_TITLE)
            type = arguments?.getString(ARG_TYPE)
            listId = arguments?.getLong(ARG_LIST_ID, -1L)?.takeIf { it != -1L }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentThreeColumnBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            adapter = BookAdapter { book ->
                BookInfoPageFragment().also { frag ->
                    frag.arguments = Bundle().apply {
                        putParcelable("book", book)
                    }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.innerFragmentContainer, frag)
                        .addToBackStack(null)
                        .commit()
                }
            }

            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            binding.recyclerView.adapter = adapter
            binding.headerTitle.text = pageTitle ?: "Books"

            val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)

                if (listId != null) {
                    Log.d("THREE_COLUMN", "Liste ID ile kitaplar yüklenecek: $listId")
                    // TODO: listId'ye özel kitapları getir – ViewModel/metodun hazır olması gerekir.

                }

                else if (type == "read") {
                    val factory = ReadViewModelFactory(requireContext())
                    readViewModel = ViewModelProvider(this, factory)[ReadViewModel::class.java]

                    readViewModel.readList.observe(viewLifecycleOwner) { entries ->
                        val books = entries.map { entry ->
                            Book(

                                id = entry.id,
                                author = entry.bookAuthor ?: "Unknown",
                                title = entry.bookTitle ?: "Untitled",
                                isbn = entry.bookIsbn ?: "",
                                description = entry.bookDescription ?: "No description available",
                                coverImageUrl = entry.bookCoverUrl,
                                pageCount = entry.bookPageCount ?: 0,
                                publisher = entry.bookPublisher ?: "Unknown publisher",
                                publishedYear = entry.bookPublishedYear ?: 0,
                                rating = entry.rating
                            )
                        }
                        adapter.submitList(books)
                    }


                    readViewModel.error.observe(viewLifecycleOwner) {
                        Log.e("THREE_COLUMN", "Hata: $it")
                    }

                    if (userId != -1L) {
                        readViewModel.loadReadList(userId)
                    }
                }
                else {
                    bookViewModel.books.observe(viewLifecycleOwner) { books ->
                        Log.d("THREE_COLUMN", "Gelen kitap sayısı: ${books.size}")
                        adapter.submitList(books)
                    }
                    bookViewModel.fetchBooks(type ?: "fiction")
                }
            }

            override fun onDestroyView() {
                _binding = null
                super.onDestroyView()
            }
        }