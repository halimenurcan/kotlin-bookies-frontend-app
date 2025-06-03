package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentHomePageBinding

class MainPageFragment : Fragment() {

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

        // Başlangıçta Books sayfasını göster
        loadInnerFragment(InnerBooksFragment())

        // Header içindeki butonlara erişim
        val booksBtn = binding.root.findViewById<View>(R.id.booksButton)
        val reviewsBtn = binding.root.findViewById<View>(R.id.reviewsButton)
        val listsBtn = binding.root.findViewById<View>(R.id.listsButton)

        booksBtn.setOnClickListener {
            loadInnerFragment(InnerBooksFragment())
        }

        reviewsBtn.setOnClickListener {
            loadInnerFragment(ReviewsFragment())
        }

        listsBtn.setOnClickListener {
            loadInnerFragment(ListsFragment())
        }
    }

    private fun loadInnerFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(binding.innerFragmentContainer.id, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
