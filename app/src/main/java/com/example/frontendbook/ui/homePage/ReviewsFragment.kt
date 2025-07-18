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
    private val args: ReviewsFragmentArgs by navArgs()
    private val viewModel: ReviewsViewModel by viewModels { ReviewsViewModelFactory(requireContext()) }
    private lateinit var adapter: ReviewsAdapter
    private val likedReviewIds = mutableListOf<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d("REVIEWS_FRAGMENT", "onCreateView called")
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("REVIEWS_FRAGMENT", "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        Log.d("REVIEWS_FRAGMENT", "userId from SharedPreferences: $userId")

        if (userId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı oturumu bulunamadı!", Toast.LENGTH_LONG).show()
            Log.e("REVIEWS_FRAGMENT", "HATA: user_id bulunamadı, çıkılıyor.")
            return
        }

        val likedRepo = LikedReviewsRepository(RetrofitClient.likedReviewsApiService(requireContext()))
        Log.d("REVIEWS_FRAGMENT", "LikedReviewsRepository oluşturuldu.")

        // 1. BEĞENİLEN REVIEWS ID'LERİNİ ÇEK ve ADAPTER'I OLUŞTUR
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val likedReviewIds = likedRepo.getLikedReviewIds(userId) // suspend fonksiyon
                Log.d("REVIEWS_FRAGMENT", "Kullanıcının beğendiği yorum id'leri: $likedReviewIds")

                adapter = ReviewsAdapter(
                    likedRepo = likedRepo,
                    userId = userId,
                    likedReviewIds = likedReviewIds.toMutableList(),
                    onLikedChanged = {
                        // Beğeni değiştiğinde güncel id listesini tekrar çek ve adapter'ı güncelle!
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

                // YORUMLARI GÖZLE
                viewModel.comments.observe(viewLifecycleOwner) { comments: List<ReviewDto> ->
                    Log.d("REVIEWS_FRAGMENT", "viewModel.comments.observe çalıştı, yorum sayısı: ${comments.size}")
                    adapter.submitList(comments)
                }
                viewModel.error.observe(viewLifecycleOwner) { msg ->
                    msg?.let {
                        Log.e("REVIEWS_FRAGMENT", "viewModel.error: $it")
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                }

                val book = args.book
                Log.d("REVIEWS_FRAGMENT", "Args.book id: ${book?.id}, title: ${book?.title}")
                if (book != null && book.id != 0L && book.id != -1L) {
                    Log.d("REVIEWS_FRAGMENT", "Yorumlar bu kitap için yükleniyor, id=${book.id}")
                    viewModel.loadCommentsForBook(book.id)
                } else {
                    Log.d("REVIEWS_FRAGMENT", "Kitap ID geçersiz, tüm yorumlar yükleniyor.")
                    viewModel.loadAllComments()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Beğenilenler alınamadı: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        Log.d("REVIEWS_FRAGMENT", "onDestroyView çağrıldı")
        super.onDestroyView()
        _binding = null
    }
}
