package com.example.frontendbook.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontendbook.databinding.FragmentThreeColumnBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.adapter.BookAdapter


class ThreeColumnFragment : Fragment() {

    private var _binding: FragmentThreeColumnBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BookAdapter
    private var pageTitle: String? = null

    companion object {
        private const val ARG_TITLE = "arg_title"

        fun newInstance(title: String, type: String): ThreeColumnFragment {
            val fragment = ThreeColumnFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageTitle = arguments?.getString(ARG_TITLE)
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

        adapter = BookAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = adapter

        binding.headerTitle.text = pageTitle ?: "Books"

        // Dummy veri örneği (gerçekte repo'dan veri gelir)
        val dummyBooks = List(12) {
            Book(
                title = "Book ${it + 1}",
                author = "Author ${it + 1}",
                year = 2000 + it,
                genre = "Fiction",
                country = "USA",
                language = "English",
                popularity = (50..100).random(),
                rating = (3..5).random() + listOf(0.0, 0.5).random(),
                imageUrl = null
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
