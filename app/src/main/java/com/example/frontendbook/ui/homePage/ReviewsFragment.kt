package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentReviewsBinding
import com.example.frontendbook.data.model.ReviewDto
import com.example.frontendbook.ui.homePage.adapter.ReviewsAdapter

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReviewsViewModel
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val singleReviewId = arguments?.getLong("review_id", -1L) ?: -1L

        adapter = ReviewsAdapter(emptyList()) { review: ReviewDto -> /*...*/ }

        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReviewsFragment.adapter
        }

        viewModel = ViewModelProvider(this, ReviewsViewModelFactory(requireContext()))
            .get(ReviewsViewModel::class.java)

        viewModel.reviews.observe(viewLifecycleOwner) { list ->
            if (singleReviewId != -1L) {
                val filtered = list.find { it.id == singleReviewId }
                adapter.submitList(filtered?.let { listOf(it) } ?: emptyList())
            } else {
                adapter.submitList(list)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        viewModel.loadAllReviews()
        // Adapter boş başlat
        adapter = ReviewsAdapter(emptyList()) { review: ReviewDto ->
            // tıklama işlemi
        }

        // RecyclerView ayarları
        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReviewsFragment.adapter
        }

        // ViewModel kurulumu
        viewModel = ViewModelProvider(this, ReviewsViewModelFactory(requireContext()))
            .get(ReviewsViewModel::class.java)

        // LiveData gözle
        viewModel.reviews.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // Veri yükle
        // Tüm yorumlar:
        viewModel.loadAllReviews()
        // veya belirli kitap:
        // val bookId = arguments?.getLong("bookId") ?: -1L
        // viewModel.loadReviewsForBook(bookId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
