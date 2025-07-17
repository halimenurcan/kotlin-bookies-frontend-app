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
        Log.d("REVIEWS_FRAGMENT", "onCreateView called")
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("REVIEWS_FRAGMENT", "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)
        // Kullanıcı ID'yi SharedPreferences'tan çek
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        Log.d("REVIEWS_FRAGMENT", "userId from SharedPreferences: $userId")

        // User id kontrol
        if (userId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı oturumu bulunamadı!", Toast.LENGTH_LONG).show()
            Log.e("REVIEWS_FRAGMENT", "HATA: user_id bulunamadı, çıkılıyor.")
            return
        }

        // LikedReviewsRepository oluştur
        val likedRepo = LikedReviewsRepository(RetrofitClient.likedReviewsApiService(requireContext()))
        Log.d("REVIEWS_FRAGMENT", "LikedReviewsRepository oluşturuldu.")

        // Adapter oluştur (userId ile)
        adapter = ReviewsAdapter(
            likedRepo = likedRepo,
            userId = userId,
            onLikedChanged = {
                // Yorumları güncellediğimiz fonksiyon!
                viewModel.loadAllComments()
            }
        )
        Log.d("REVIEWS_FRAGMENT", "ReviewsAdapter oluşturuldu, userId=$userId")

        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ReviewsFragment.adapter
            Log.d("REVIEWS_FRAGMENT", "RecyclerView adapter set edildi.")
        }

        // LiveData gözlemleri
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
    }

    override fun onDestroyView() {
        Log.d("REVIEWS_FRAGMENT", "onDestroyView çağrıldı")
        super.onDestroyView()
        _binding = null
    }
}
