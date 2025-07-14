package com.example.frontendbook.ui.homePage

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.databinding.FragmentListsBinding
import com.example.frontendbook.ui.base.adapter.OtherListAdapter

class OtherListsFragment : Fragment() {

    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListsViewModel
    private lateinit var otherListAdapter: OtherListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddList.visibility = View.GONE // Diğer kullanıcıların listesi olduğu için gizli

        // ViewModel
        viewModel = ViewModelProvider(
            this,
            ListsViewModelFactory(requireContext())
        )[ListsViewModel::class.java]

        // Adapter
        otherListAdapter = OtherListAdapter(
            lists = emptyList(),
            onFollowClick = { listDto ->
                Toast.makeText(requireContext(), "Followed ${listDto.title}", Toast.LENGTH_SHORT).show()
            },
            onBookClick = { bookId ->
                Toast.makeText(requireContext(), "Book $bookId clicked", Toast.LENGTH_SHORT).show()
            }
        )

        binding.listsRecyclerView.apply {
            adapter = otherListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // ViewModel’den diğer kullanıcıların listelerini iste
        viewModel.loadOtherLists()

        // Gözlemle
        viewModel.otherLists.observe(viewLifecycleOwner) { lists ->
            otherListAdapter.submitList(lists)

            // Kitapları yatay listeye manuel ekle
            binding.listsRecyclerView.post {
                lists.forEachIndexed { index, listDto ->
                    val recyclerItem = binding.listsRecyclerView.layoutManager?.findViewByPosition(index)
                    recyclerItem?.let {
                        val bookContainer = it.findViewById<LinearLayout>(R.id.bookContainer)
                        populateBooks(listDto, bookContainer)
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun populateBooks(listDto: ListDto, container: LinearLayout) {
        // Güvenli silme
        if (container.childCount > 2) {
            container.removeViews(1, container.childCount - 2)
        }

        val inflater = LayoutInflater.from(context)
        listDto.books.forEach { book ->
            val bookView = inflater.inflate(R.layout.item_book_grid, container, false)
            // ... kitap görsellerini doldur
            container.addView(bookView, container.childCount - 1)
        }

        // SeeMoreCard tıklama
        val seeMoreCard = container.findViewById<View>(R.id.seeMoreCard)
        seeMoreCard?.setOnClickListener {
            val action = OtherListsFragmentDirections.actionOtherListsFragmentToThreeColumnFragment(
                title = listDto.title,
                listId = listDto.id,
                type = null
            )
            requireParentFragment().findNavController().navigate(action)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
