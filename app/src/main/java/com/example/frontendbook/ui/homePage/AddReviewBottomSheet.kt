package com.example.frontendbook.ui.homePage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.frontendbook.databinding.BottomSheetAddBookBinding
import com.example.frontendbook.data.model.ReviewCreateRequest
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class AddReviewBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddBookBinding? = null
    private val binding get() = _binding!!

    // Share the same ReviewsViewModel with the parent Activity/Fragment
    private val viewModel: ReviewsViewModel by activityViewModels {
        ReviewsViewModelFactory(requireContext())
    }

    companion object {
        private const val ARG_BOOK_ID = "bookId"
        fun newInstance(bookId: Long): AddReviewBottomSheet =
            AddReviewBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(ARG_BOOK_ID, bookId)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookId = arguments?.getLong(ARG_BOOK_ID) ?: -1L

        // Update title text
        binding.addBookTitle.text = "Add Review"

        // When “Done” is clicked, fire createReview
        binding.saveBookButton.setOnClickListener {
            val text = binding.commentInput.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Review boş olamaz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rating = binding.ratingBar.rating.toInt()

            val prefs = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)
            if (userId == -1L) {
                Toast.makeText(requireContext(), "Oturum açılmamış", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val req = ReviewCreateRequest(
                userId  = userId,
                bookId  = bookId,
                score   = rating,
                comment = text
            )
            viewModel.createComment(req)
        }

        // Observe the newly created review
        viewModel.single.observe(viewLifecycleOwner, Observer { created ->
            Toast.makeText(
                requireContext(),
                "Review eklendi (ID=${created.id})",
                Toast.LENGTH_SHORT
            ).show()
            // Refresh the list
            viewModel.loadCommentsForBook(bookId)
            dismiss()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
