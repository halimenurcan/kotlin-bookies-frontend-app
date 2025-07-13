package com.example.frontendbook.ui.base.threecolumn

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ListsRepository
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
    private lateinit var threeColumnViewModel: ThreeColumnViewModel

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var readViewModel: ReadViewModel
    private lateinit var adapter: BookAdapter

    // navigation arg name'leriyle birebir eşleşiyor
    companion object {
        const val ARG_TITLE = "title"
        const val ARG_TYPE = "type"
        const val ARG_LIST_ID = "listId"
        const val ARG_USER_ID = "userId"

        fun newInstance(
            title: String,
            type: String? = null,
            listId: Long? = null,
            userId: Long? = null
        ): ThreeColumnFragment {
            return ThreeColumnFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    type?.let { putString(ARG_TYPE, it) }
                    listId?.let { putLong(ARG_LIST_ID, it) }
                    userId?.let { putLong(ARG_USER_ID, it) }
                }
            }
        }
    }

    private var pageTitle: String? = null
    private var type: String? = null
    private var listId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pageTitle = arguments?.getString(ARG_TITLE)
        type = arguments?.getString(ARG_TYPE)
        val rawListId = arguments?.getLong(ARG_LIST_ID, -1L) ?: -1L
        listId = if (rawListId != -1L) rawListId else null

        Log.d("THREE_COLUMN", "onCreate args => title=$pageTitle, type=$type, listId=$listId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThreeColumnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter { book ->
            val action = ThreeColumnFragmentDirections
                .actionThreeColumnFragmentToBookInfoPageFragment(book)
            findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter
        binding.headerTitle.text = pageTitle ?: "Books"

        val api = RetrofitClient.listsApiService(requireContext())
        val repository = ListsRepository(api)
        val factory = ThreeColumnViewModelFactory(repository)
        threeColumnViewModel = ViewModelProvider(this, factory)[ThreeColumnViewModel::class.java]

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)

        when {
            listId != null -> {
                Log.d("THREE_COLUMN", "📘 Liste ID ile kitaplar yüklenecek: $listId")
                threeColumnViewModel.fetchListWithBooks(listId!!)
                threeColumnViewModel.listWithBooks.observe(viewLifecycleOwner) { listDto ->
                    Log.d("THREE_COLUMN", "📚 Liste verisi geldi: ${listDto.books.size} kitap")
                    val books = listDto.books.map {
                        Book(
                            id = it.id,
                            author =  "Unknown", // ✅ null check
                            title = it.title,
                            isbn = it.isbn ?: "",
                            description = it.description ?: "No description available", // ✅ null check
                            coverImageUrl = it.coverImageUrl,
                            pageCount = it.pageCount ?: 0,
                            publisher = "Unknown publisher",
                            publishedYear = it.publishedYear ?: 0,
                            rating = it.rating ?: 0
                        )
                    }
                    adapter.submitList(books)
                }
                threeColumnViewModel.error.observe(viewLifecycleOwner) {
                    Log.e("THREE_COLUMN", "❌ Hata: $it")
                }
            }

            type == "read" -> {
                Log.d("THREE_COLUMN", "📗 Read list yüklenecek (userId=$userId)")
                val readFactory = ReadViewModelFactory(requireContext())
                readViewModel = ViewModelProvider(this, readFactory)[ReadViewModel::class.java]
                readViewModel.readList.observe(viewLifecycleOwner) { entries ->
                    Log.d("THREE_COLUMN", "📚 Read list verisi geldi: ${entries.size}")
                    val books = entries.map { entry ->
                        Book(
                            id = entry.id,
                            author = entry.bookAuthor ?: "Unknown",
                            title = entry.bookTitle ?: "Untitled",
                            isbn = entry.bookIsbn ?: "",
                            description = entry.bookDescription ?: "No description",
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
                    Log.e("THREE_COLUMN", "❌ Hata: $it")
                }
                if (userId != -1L) {
                    readViewModel.loadReadList(userId)
                }
            }

            else -> {
                Log.d("THREE_COLUMN", "📕 Genel kitaplar yüklenecek, type=$type")
                bookViewModel.books.observe(viewLifecycleOwner) { books ->
                    Log.d("THREE_COLUMN", "📚 Gelen kitap sayısı: ${books.size}")
                    adapter.submitList(books)
                }
                bookViewModel.fetchBooks(type ?: "fiction")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
