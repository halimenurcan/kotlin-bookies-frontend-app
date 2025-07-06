// ui/homePage/AllReviewsFragment.kt

package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentReviewsBinding

class AllReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewsViewModel by viewModels {
        ReviewsViewModelFactory(requireContext())
    }
    private lateinit var reviewsAdapter: ReviewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SystemBars padding (isterseniz kopyalayın)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // RecyclerView + Adapter
        reviewsAdapter = ReviewsAdapter()
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
        viewModel.loadAllReviews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
