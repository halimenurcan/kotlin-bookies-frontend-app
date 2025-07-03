package com.example.frontendbook.ui.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.frontendbook.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontendbook.databinding.FragmentThreeColumnBinding
import com.example.frontendbook.ui.base.adapter.BookAdapter
import com.example.frontendbook.ui.bookInfoPage.BookInfoPageFragment
import com.example.frontendbook.ui.viewmodel.BookViewModel

class ThreeColumnFragment : Fragment() {

    private var _binding: FragmentThreeColumnBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookViewModel by viewModels()

    private lateinit var adapter: BookAdapter
    private var pageTitle: String? = null
    private var type: String? = null

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_TYPE = "arg_type"

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

        // 1. Adapter'ı oluştur
        adapter = BookAdapter { book ->
            // Tıklanırsa BookInfoPage'e geç
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

        // 2. RecyclerView'a bağla
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        // 3. Başlığı ata
        binding.headerTitle.text = pageTitle ?: "Books"

        // 4. Veriyi gözlemle
        viewModel.books.observe(viewLifecycleOwner) { books ->
            Log.d("THREE_COLUMN", "Gelen kitap sayısı: ${books.size}")
            adapter.submitList(books)
        }

        // 5. Veri iste
        viewModel.fetchBooks(type ?: "fiction")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
