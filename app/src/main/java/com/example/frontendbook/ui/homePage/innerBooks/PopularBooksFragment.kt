package com.example.frontendbook.ui.homePage.innerBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.frontendbook.databinding.FragmentPopularBooksBinding

class PopularBooksFragment : Fragment() {

    private var _binding: FragmentPopularBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyBooks = List(24) { i -> "Book ${i + 1}" }

        binding.popularBooksRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.popularBooksRecyclerView.adapter = PopularBooksAdapter(dummyBooks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
