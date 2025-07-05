package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentHomePageBinding

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SystemBars padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        // Başlangıçta InnerBooksFragment
        loadInnerFragment(InnerBooksFragment.newInstance("All Books", "fiction"))
        updateButtonColors(binding.header.booksButton)

        // Button click’leri
        binding.header.booksButton.setOnClickListener {
            loadInnerFragment(InnerBooksFragment.newInstance("All Books", "fiction"))
            updateButtonColors(binding.header.booksButton)
        }

        binding.header.reviewsButton.setOnClickListener {
            loadInnerFragment(ReviewsFragment())
            updateButtonColors(binding.header.reviewsButton)
        }

        binding.header.listsButton.setOnClickListener {
            loadInnerFragment(ListsFragment.newInstance(showUserLists = false)) // 👈 BURASI
            updateButtonColors(binding.header.listsButton)
        }

    }

    private fun loadInnerFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(binding.innerFragmentContainer.id, fragment)
            .commit()
    }

    private fun updateButtonColors(selectedButton: View) {
        val ctx = requireContext()
        val active = ContextCompat.getColor(ctx, R.color.buttonSecondary)
        val inactive = ContextCompat.getColor(ctx, R.color.buttonPrimary)
        listOf(binding.header.booksButton, binding.header.reviewsButton, binding.header.listsButton)
            .forEach { btn ->
                btn.setBackgroundColor(if (btn == selectedButton) active else inactive)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
