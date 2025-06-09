package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // 🔶 TEST AMAÇLI: Dummy kitapla BookInfoPage'e geçiş
        val dummyBook = Book(
            title = "Test Book",
            author = "Author Name",
            year = 2023,
            genre = "Fiction",
            country = "UK",
            language = "English",
            popularity = 90,
            rating = 4.0,
            imageUrl = null,
            pageCount = 250,
            description = "This is a test book used for navigation test."
        )

        binding.root.setOnLongClickListener {
            val bundle = Bundle().apply {
                putParcelable("book", dummyBook)
            }
            findNavController().navigate(R.id.bookInfoPageFragment, bundle)
            true
        }
        // 🔶 Bu kısım eklendi: HomePage'te uzun basınca kitap detay sayfasına geçiş yapılır
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
