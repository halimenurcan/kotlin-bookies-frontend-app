package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.databinding.FragmentReviewsBinding

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_BOOK_ID = "arg_book_id"
        fun newInstance(bookId: Long) = ReviewsFragment().apply {
            arguments = Bundle().apply { putLong(ARG_BOOK_ID, bookId) }
        }
    }

    private val viewModel: ReviewsViewModel by viewModels { ReviewsViewModelFactory(requireContext()) }
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView ve Adapter
        adapter = ReviewsAdapter()
        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReviewsFragment.adapter
        }

        // LiveData gözlemleri
        viewModel.comments.observe(viewLifecycleOwner) { comments: List<ReviewDto> ->

            adapter.submitList(comments)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // Manuel argüman ile bookId al ve yorumları yükle
        val bookId = arguments?.getLong(ARG_BOOK_ID) ?: -1L
        if (bookId != -1L) {
            viewModel.loadCommentsForBook(bookId)
        } else {
            Toast.makeText(requireContext(), "Kitap ID bulunamadı", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}