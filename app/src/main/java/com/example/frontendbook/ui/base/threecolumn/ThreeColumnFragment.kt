package com.example.frontendbook.ui.common

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
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.databinding.FragmentThreeColumnBinding
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

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_TYPE = "arg_type"

        /**
         * Bu fonksiyon, bu fragment'i title ve type argümanlarıyla oluşturmak için kullanılır.
         * InnerBooksFragment gibi yerlerden çağırılabilir.
         */
        fun newInstance(title: String, type: String): ThreeColumnFragment {
            val fragment = ThreeColumnFragment()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_TYPE, type)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageTitle = arguments?.getString(ARG_TITLE)
        type = arguments?.getString(ARG_TYPE)
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

        // Kitap kartlarını gösterecek adapter
        adapter = BookAdapter { book ->
            val fragment = BookInfoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("book", book)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.innerFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        // RecyclerView ayarları
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        // Başlığı ayarla
        binding.headerTitle.text = pageTitle ?: "Books"

        // SharedPreferences üzerinden kullanıcı ID'sini al
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)

        if (type == "read") {
            // Eğer "read" listesi ise ReadViewModel kullan
            val factory = ReadViewModelFactory(requireContext())
            readViewModel = ViewModelProvider(this, factory)[ReadViewModel::class.java]

            readViewModel.readList.observe(viewLifecycleOwner) { entries ->
                val books = entries.map {
                    Book(
                        id = it.id ?: 0L,
                        title = it.bookTitle,
                        author = "Unknown",
                        year = 0,
                        genre = null,
                        country = null,
                        language = null,
                        popularity = 0,
                        rating = 0.0,
                        imageUrl = it.bookCoverUrl,
                        pageCount = 0,
                        description = "No description available"
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

        } else {
            // Diğer türlerde BookViewModel kullan
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
