package com.example.frontendbook.ui.homePage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedReviewsRepository
import com.example.frontendbook.databinding.FragmentReviewsBinding
import kotlinx.coroutines.launch

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ReviewsAdapter
    private val viewModel: ReviewsViewModel by viewModels { ReviewsViewModelFactory(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviewId = arguments?.getLong("reviewId", -1) ?: -1
        Log.d("REVIEW_FRAG", "Received reviewId=$reviewId")

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)

        if (userId == -1L) {
            Toast.makeText(requireContext(), "User session not found!", Toast.LENGTH_LONG).show()
            return
        }

        val likedRepo = LikedReviewsRepository(RetrofitClient.likedReviewsApiService(requireContext()))

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val likedReviewIds = likedRepo.getLikedReviewIds(userId)
                Log.d("REVIEW_FRAG", "Liked review IDs: $likedReviewIds")

                adapter = ReviewsAdapter(
                    likedRepo = likedRepo,
                    userId = userId,
                    likedReviewIds = likedReviewIds.toMutableList(),
                    onLikedChanged = {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val newLikedReviewIds = likedRepo.getLikedReviewIds(userId)
                            adapter.updateLikedReviewIds(newLikedReviewIds)
                        }
                    }
                )

                binding.reviewsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@ReviewsFragment.adapter
                }

                viewModel.comments.observe(viewLifecycleOwner) { comments ->
                    Log.d("REVIEW_FRAG", "Observing comments: size=${comments.size}")
                    adapter.submitList(comments)
                }

                viewModel.selectedReview.observe(viewLifecycleOwner) { selected ->
                    Log.d("REVIEW_FRAG", "Observing selectedReview: $selected")
                    selected?.let {
                        adapter.submitList(listOf(it))
                    }
                }

                viewModel.error.observe(viewLifecycleOwner) {
                    it?.let { error ->
                        Log.e("REVIEW_FRAG", "Error observed: $error")
                        Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                    }
                }

                if (reviewId != -1L) {
                    viewModel.loadCommentById(reviewId)
                } else {
                    viewModel.loadAllComments()
                }

            } catch (e: Exception) {
                Log.e("REVIEW_FRAG", "Exception during view setup: ${e.message}")
                Toast.makeText(requireContext(), "Likes could not received: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
