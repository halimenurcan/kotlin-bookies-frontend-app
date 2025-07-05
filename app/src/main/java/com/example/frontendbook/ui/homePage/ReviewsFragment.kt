package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentReviewsBinding
import com.example.frontendbook.data.model.ReviewDto

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_BOOK_ID = "arg_book_id"

        fun newInstance(bookId: Long): ReviewsFragment {
            return ReviewsFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_BOOK_ID, bookId)
                }
            }
        }
    }

    private val viewModel: ReviewsViewModel by viewModels {
        ReviewsViewModelFactory(requireContext())
    }
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1️⃣ Argument’tan gerçek bookId’yi al
        val bookId = requireArguments().getLong(ARG_BOOK_ID).also {
            Log.d("ReviewsFragment", "onViewCreated for bookId=$it")
        }

        // 2️⃣ RecyclerView + Adapter



        // 3️⃣ ViewModel çağrısı
        viewModel.loadCommentsForBook(bookId)

        // 4️⃣ Observer’lar

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Log.e("ReviewsFragment", "error=$it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
