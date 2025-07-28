package com.example.frontendbook.ui.base.threecolumn

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontendbook.data.remote.AiRepository
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.data.repository.ListsRepository
import com.example.frontendbook.databinding.FragmentThreeColumnBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.adapter.BookAdapter
import com.example.frontendbook.ui.likedbooks.LikedBooksViewModel
import com.example.frontendbook.ui.profile.ReadViewModel
import com.example.frontendbook.ui.profile.ReadViewModelFactory
import com.example.frontendbook.ui.recommendation.AiRecommendationViewModelFactory
import com.example.frontendbook.ui.viewmodel.BookViewModel

class ThreeColumnFragment : Fragment() {

    private var _binding: FragmentThreeColumnBinding? = null
    private val binding get() = _binding!!
    private lateinit var threeColumnViewModel: ThreeColumnViewModel
    private lateinit var likedBooksViewModel: LikedBooksViewModel
    private lateinit var readViewModel: ReadViewModel
    private lateinit var adapter: BookAdapter
    val aiRepo = AiRepository()
    val aiViewModel: AiRecommendationViewModel by viewModels {
        AiRecommendationViewModelFactory(requireContext(), aiRepo)
    }

    private val bookViewModel: BookViewModel by viewModels()

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
    private var userId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pageTitle = it.getString(ARG_TITLE)
            type = it.getString(ARG_TYPE)
            listId = if (it.containsKey(ARG_LIST_ID)) it.getLong(ARG_LIST_ID) else null
            userId = if (it.containsKey(ARG_USER_ID)) it.getLong(ARG_USER_ID) else null
        }
        Log.d("THREE_COLUMN", "onCreate args => title=$pageTitle, type=$type, listId=$listId, userId=$userId")
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


        if (listId != null && listId != 0L) {
            Log.d("THREE_COLUMN", "Books will be loaded with List ID: $listId")
            threeColumnViewModel.fetchListWithBooks(listId!!)
            threeColumnViewModel.listWithBooks.observe(viewLifecycleOwner) { listDto ->
                val books = listDto.books.map {
                    Book(
                        id = it.id,
                        author = it.author.name ?: "Unknown",
                        title = it.title ?: "Untitled",
                        isbn = it.isbn ?: "",
                        description = it.description ?: "No description available",
                        coverImageUrl = it.coverImageUrl ?: "",
                        pageCount = it.pageCount ?: 0,
                        publisher = it.publisher ?: "Unknown publisher",
                        publishedYear = it.publishedYear ?: 0,
                        rating = it.rating ?: 0
                    )
                }
                adapter.submitList(books)
            }
            threeColumnViewModel.error.observe(viewLifecycleOwner) {

            }
        }

        else if (type == "read" || type == "readlist" || type == "likes") {
            when (type) {
                "read" -> {
                    val readFactory = ReadViewModelFactory(requireContext())
                    readViewModel = ViewModelProvider(this, readFactory)[ReadViewModel::class.java]
                    readViewModel.readBooks.observe(viewLifecycleOwner) { entries ->
                        val books = entries.map { entry ->
                            Log.d("THREE_COLUMN", " Entry: $entry")
                            Book(
                                id = entry.bookId.toLong(),
                                author = entry.bookAuthor ?: "Unknown",
                                title = entry.bookTitle ?: "Untitled",
                                isbn = entry.bookIsbn ?: "",
                                description = entry.bookDescription ?: "No description",
                                coverImageUrl = entry.bookCoverUrl,
                                pageCount = entry.bookPageCount ?: 0,
                                publisher = entry.bookPublisher ?: "Unknown publisher",
                                publishedYear = entry.bookPublishedYear ?: 0,
                                rating = 0
                            )
                        }
                        adapter.submitList(books)
                    }
                    if (userId != null && userId != -1L) readViewModel.loadReadBooks(userId!!)
                    readViewModel.error.observe(viewLifecycleOwner) {
                        Log.e("THREE_COLUMN", " Error: $it")
                    }
                }
                "readlist" -> {
                    val readFactory = ReadViewModelFactory(requireContext())
                    readViewModel = ViewModelProvider(this, readFactory)[ReadViewModel::class.java]
                    readViewModel.toReadList.observe(viewLifecycleOwner) { entries ->
                        val books = entries.map { entry ->
                            Book(
                                id = entry.bookId.toLong(),
                                author = entry.bookAuthor ?: "Unknown",
                                title = entry.bookTitle ?: "Untitled",
                                isbn = entry.bookIsbn ?: "",
                                description = entry.bookDescription ?: "No description",
                                coverImageUrl = entry.bookCoverUrl,
                                pageCount = entry.bookPageCount ?: 0,
                                publisher = entry.bookPublisher ?: "Unknown publisher",
                                publishedYear = entry.bookPublishedYear ?: 0,
                                rating = 0
                            )
                        }
                        adapter.submitList(books)
                    }
                    if (userId != null && userId != -1L) readViewModel.loadToReadList(userId!!)
                    readViewModel.error.observe(viewLifecycleOwner) {
                        Log.e("THREE_COLUMN", " Error: $it")
                    }
                }
                "likes" -> {
                    val likedRepo = LikedBooksRepository(RetrofitClient.likedBooksApiService(requireContext()))
                    likedBooksViewModel = ViewModelProvider(
                        this,
                        object : ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return LikedBooksViewModel(likedRepo) as T
                            }
                        }
                    )[LikedBooksViewModel::class.java]

                    likedBooksViewModel.likedBooks.observe(viewLifecycleOwner) { books ->
                        adapter.submitList(books)
                    }
                    likedBooksViewModel.error.observe(viewLifecycleOwner) {
                        Log.e("THREE_COLUMN", " Error: $it")
                    }
                    if (userId != null && userId != -1L) likedBooksViewModel.loadLikedBooks(userId!!)
                }
            }
        }
        else if (type == "ai") {
            aiViewModel.recommendedBooks.observe(viewLifecycleOwner) { books ->
                adapter.submitList(books)
            }

            aiViewModel.error.observe(viewLifecycleOwner) { err ->
                err?.let {
                    Toast.makeText(requireContext(), "AI Error: $it", Toast.LENGTH_SHORT).show()
                }
            }

            val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)
            if (userId != -1L) {
                aiViewModel.fetchRecommendationsFromLikedBooks(userId)
            }
        }
        else {
            bookViewModel.books.observe(viewLifecycleOwner) { books ->
                adapter.submitList(books)
            }
            bookViewModel.fetchBooks(type ?: "fiction")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
