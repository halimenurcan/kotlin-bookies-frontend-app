package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentReviewsBinding
import com.example.frontendbook.ui.base.adapter.ReviewsAdapter

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewsRecyclerView.adapter = ReviewsAdapter(generateDummyReviews())
    }

    private fun generateDummyReviews(): List<String> {
        return List(10) { "Review ${it + 1}: Bu kitap harikaydı!" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
