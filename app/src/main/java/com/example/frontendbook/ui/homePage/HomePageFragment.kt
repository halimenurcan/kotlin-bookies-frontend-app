package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentHomePageBinding
import com.example.frontendbook.domain.model.Book

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

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // İlk açılışta Books fragment'ı yükle ve butonu turuncu yap
        loadInnerFragment(InnerBooksFragment())
        updateButtonColors(binding.header.booksButton)

        // Butonlara tıklama
        binding.header.booksButton.setOnClickListener {
            loadInnerFragment(InnerBooksFragment())
            updateButtonColors(binding.header.booksButton)
        }

        binding.header.reviewsButton.setOnClickListener {
            loadInnerFragment(ReviewsFragment())
            updateButtonColors(binding.header.reviewsButton)
        }

        binding.header.listsButton.setOnClickListener {
            loadInnerFragment(ListsFragment())
            updateButtonColors(binding.header.listsButton)
        }

    }

    private fun loadInnerFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(binding.innerFragmentContainer.id, fragment)
            .commit()
    }

    private fun updateButtonColors(selectedButton: View) {
        val context = requireContext()
        val active = ContextCompat.getColor(context, R.color.buttonSecondary)
        val inactive = ContextCompat.getColor(context, R.color.buttonPrimary)

        val allButtons = listOf(
            binding.header.booksButton,
            binding.header.reviewsButton,
            binding.header.listsButton
        )

        allButtons.forEach { button ->
            button.setBackgroundColor(if (button == selectedButton) active else inactive)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
