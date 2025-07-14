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
import com.example.frontendbook.data.api.dto.ReviewDto
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedReviewsRepository
import com.example.frontendbook.databinding.FragmentReviewsBinding

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val args: ReviewsFragmentArgs by navArgs()
    private val viewModel: ReviewsViewModel by viewModels { ReviewsViewModelFactory(requireContext()) }
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Test amaçlı sabit USER_ID (uygulamanın bir yerinden gerçek kullanıcı ID'sini almalısın)
        val CURRENT_USER_ID = 123L

        // LikedReviewsRepository oluştur
        val likedRepo = LikedReviewsRepository(RetrofitClient.likedReviewsApiService(requireContext()))

        // Adapter oluştur
        adapter = ReviewsAdapter(
            likedRepo = likedRepo,
            userId = CURRENT_USER_ID
        )

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
        Log.d("ReviewsFragment", "Gelen kitap id: ${book?.id}, title: ${book?.title}")
        if (book != null && book.id != 0L && book.id != -1L) {
            viewModel.loadCommentsForBook(book.id)
        } else {
            Log.d("ReviewsFragment", "Kitap ID geçersiz, tüm yorumlar yükleniyor.")
            viewModel.loadAllComments()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
