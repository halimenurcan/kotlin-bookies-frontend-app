package com.example.frontendbook.ui.homePage.innerBooks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentInnerBooksBinding
import com.example.frontendbook.ui.common.ThreeColumnFragment

class InnerBooksFragment : Fragment() {

    private var _binding: FragmentInnerBooksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInnerBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        // Veriler XML'e statik include ile yerleştirildiği için adapter gerekmez.
    }

    private fun setupListeners() {
        binding.popularArrow.setOnClickListener {
            openThreeColumnPage("Popular This Week", "popular")
        }

        binding.exploreArrow.setOnClickListener {
            openThreeColumnPage("Explore More", "explore")
        }
        binding.exploreSeeAll.setOnClickListener {
            openThreeColumnPage("Explore More", "explore")
        }

    }

    private fun openThreeColumnPage(title: String, type: String) {
        val fragment = ThreeColumnFragment.newInstance(title, type)
        parentFragmentManager.beginTransaction()
            .replace(R.id.innerFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
