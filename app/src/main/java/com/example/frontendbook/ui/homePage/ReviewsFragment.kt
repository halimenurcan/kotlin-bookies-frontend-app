package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bookId argument’ı alın
        val bookId = arguments?.getLong("bookId") ?: -1L

        // RecyclerView + Adapter
        adapter = ReviewsAdapter(emptyList()) { review: ReviewDto ->
            // TODO: review item tıklama işlemi
        }
        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReviewsFragment.adapter
        }

        // ViewModel
        viewModel = ViewModelProvider(
            this,
            ReviewsViewModelFactory(requireContext())
        ).get(ReviewsViewModel::class.java)

        // Listeyi yükle ve gözle
        viewModel.loadCommentsForBook(bookId)
        viewModel.comments.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // If you want to open the BottomSheet from here, call:
        // AddReviewBottomSheet.newInstance(bookId)
        //   .show(parentFragmentManager, "AddReview")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
