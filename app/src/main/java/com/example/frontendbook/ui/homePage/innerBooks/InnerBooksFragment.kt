package com.example.frontendbook.ui.homePage.innerBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentInnerBooksBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.adapter.BookAdapter

class InnerBooksFragment : Fragment() {

    private var _binding: FragmentInnerBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularAdapter: BookAdapter
    private lateinit var exploreAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInnerBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupListeners()
        loadDummyData()
    }

    private fun setupAdapters() {
        // 📚 Popular yatay liste
        popularAdapter = BookAdapter()
        binding.popularBooksRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.popularBooksRecyclerView.adapter = popularAdapter

        // 📚 Explore 3 sütun grid liste
        exploreAdapter = BookAdapter()
        binding.exploreBooksRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.exploreBooksRecyclerView.adapter = exploreAdapter
    }

    private fun setupListeners() {
        binding.popularArrow.setOnClickListener {
            openThreeColumnPage("Popular This Week", "popular")
        }

        binding.exploreArrow.setOnClickListener {
            openThreeColumnPage("Explore More", "explore")
        }
    }

    private fun openThreeColumnPage(title: String, type: String) {
        val fragment = ThreeColumnFragment.newInstance(title, type)
        parentFragmentManager.beginTransaction()
            .replace(R.id.innerFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadDummyData() {
        val dummyBooks = List(12) { index ->
            Book(
                title = "Book ${index + 1}",
                author = "Author ${index + 1}",
                year = 2000 + index,
                genre = "Genre",
                country = "Country",
                language = "EN",
                popularity = (50..100).random(),
                rating = (3..5).random().toDouble()
            )
        }

        popularAdapter.submitList(dummyBooks)
        exploreAdapter.submitList(dummyBooks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
