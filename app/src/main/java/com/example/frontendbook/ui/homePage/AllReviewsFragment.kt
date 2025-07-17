// ui/homePage/AllReviewsFragment.kt

package com.example.frontendbook.ui.homePage

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentReviewsBinding
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedReviewsRepository
import com.example.frontendbook.domain.model.Book


class AllReviewsFragment : Fragment() {
    interface BookClickListener {
        fun onBookClicked(book: Book)
    }
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewsViewModel by viewModels {
        ReviewsViewModelFactory(requireContext())
    }
    private lateinit var reviewsAdapter: ReviewsAdapter
    private var listener: BookClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = when {
            context is BookClickListener -> context
            parentFragment is BookClickListener -> parentFragment as BookClickListener
            else -> null
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SystemBars padding (opsiyonel)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // Like işlemleri için repo ve userId
        val likedRepo = LikedReviewsRepository(RetrofitClient.likedReviewsApiService(requireContext()))
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        // GÜNCEL: Adapter'a tüm parametreleri ver!
        reviewsAdapter = ReviewsAdapter(
            likedRepo = likedRepo,
            userId = userId,
            onClick = { book ->
                listener?.onBookClicked(book)
            }
        )

        binding.reviewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewsAdapter
        }

        // LiveData gözlemleri
        viewModel.comments.observe(viewLifecycleOwner) { list ->
            reviewsAdapter.submitList(list)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        // Tüm yorumları yükle
        viewModel.loadAllComments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
