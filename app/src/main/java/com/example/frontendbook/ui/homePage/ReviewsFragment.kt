package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.databinding.FragmentReviewsBinding

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val args: ReviewsFragmentArgs by navArgs()

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
        val book = args.book
        Log.d("ReviewsFragment", "book.id: ${book.id}")

        if (book.id != 0L && book.id != -1L) {
            viewModel.loadCommentsForBook(book.id)
        } else {
            Toast.makeText(requireContext(), "Kitap ID bulunamadı", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}