package com.example.frontendbook.ui.homePage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ListFollowsRepository
import com.example.frontendbook.databinding.FragmentListsBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.base.adapter.OtherListAdapter
import com.example.frontendbook.ui.listdetail.ListFollowViewModel

class OtherListsFragment : Fragment() {
    private lateinit var followViewModel: ListFollowViewModel
    private lateinit var viewModel: ListsViewModel
    private lateinit var otherListAdapter: OtherListAdapter

    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    // Takip edilen listelerin ID'lerini burada tutuyoruz:

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddList.visibility = View.GONE

        // ViewModel'ler
        viewModel = ViewModelProvider(this, ListsViewModelFactory(requireContext()))
            .get(ListsViewModel::class.java)
        val repo = ListFollowsRepository(RetrofitClient.listFollowsApiService(requireContext()))
        followViewModel = ListFollowViewModel(repo)

        // Aktif kullanıcıyı bul
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val currentUserId = prefs.getLong("user_id", -1L)

        // ADAPTER
        otherListAdapter = OtherListAdapter(
            lists = emptyList(),
            onFollowClick = { listDto ->
                if (currentUserId != -1L) {
                    Log.d("OtherListsFragment", "Clicked follow/unfollow for: ${listDto.id}")
                    followViewModel.toggleFollow(currentUserId, listDto.id)
                } else {
                    Toast.makeText(requireContext(), "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show()
                }
            },
            onBookClick = { book ->
                val domainBook = Book(
                    id = book.id,
                    title = book.title ?: "",
                    isbn = book.isbn ?: "",
                    coverImageUrl = book.coverImageUrl ?: "",
                    author = book.author?.toString() ?: "",
                    description = book.description ?: "",
                    pageCount = book.pageCount ?: 0,
                    publisher = book.publisher ?: "",
                    publishedYear = book.publishedYear ?: 0,
                    rating = book.rating ?: 0
                )

                val action = OtherListsFragmentDirections
                    .actionOtherListsFragmentToBookDetailFragment(domainBook)
                findNavController().navigate(action)
            }
        )

        binding.listsRecyclerView.apply {
            adapter = otherListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.loadOtherLists()

        if (currentUserId != -1L) {
            followViewModel.loadFollowedListIds(currentUserId)
        }

        followViewModel.followedListIds.observe(viewLifecycleOwner) { followedIds ->
            Log.d("OtherListsFragment", "followedListIds updated: $followedIds")

            otherListAdapter.setFollowedListIds(followedIds)
            otherListAdapter.notifyDataSetChanged()
        }

        viewModel.otherLists.observe(viewLifecycleOwner) { lists ->
            otherListAdapter.submitList(lists)
            populateBookCards(lists)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show() }
        }
        followViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { Toast.makeText(requireContext(), "Takip hatası: $it", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun populateBookCards(lists: List<ListDto>) {
        binding.listsRecyclerView.post {
            lists.forEachIndexed { index, listDto ->
                val itemView = binding.listsRecyclerView.layoutManager?.findViewByPosition(index)
                itemView?.let {
                    val bookContainer = it.findViewById<LinearLayout>(R.id.bookContainer)
                    if (bookContainer != null) {
                        populateBooks(listDto, bookContainer)
                    } else {
                        Log.e("OtherListsFragment", "bookContainer is null!")
                    }
                }
            }
        }
    }

    private fun populateBooks(listDto: ListDto, container: LinearLayout) {
        if (container.childCount > 2) {
            container.removeViews(1, container.childCount - 2)
        }
        val inflater = LayoutInflater.from(context)
        listDto.books.forEach { book ->
            val bookView = inflater.inflate(R.layout.item_book_grid, container, false)
            val bookImage = bookView.findViewById<ImageView>(R.id.bookImage)
            val bookTitle = bookView.findViewById<TextView>(R.id.bookTitle)
            bookTitle.text = book.title
            Glide.with(this)
                .load(book.coverImageUrl)
                .placeholder(R.drawable.placeholder)
                .into(bookImage)
            bookView.setOnClickListener {
                val domainBook = Book(
                    id = book.id,
                    title = book.title ?: "",
                    isbn = book.isbn ?: "",
                    coverImageUrl = book.coverImageUrl ?: "",
                    author = book.author?.toString() ?: "",
                    description = book.description ?: "",
                    pageCount = book.pageCount ?: 0,
                    publisher = book.publisher ?: "",
                    publishedYear = book.publishedYear ?: 0,
                    rating = book.rating ?: 0)
                val action = OtherListsFragmentDirections
                    .actionOtherListsFragmentToBookDetailFragment(domainBook)
                findNavController().navigate(action)
            }
            container.addView(bookView, container.childCount - 1)
        }
        val seeMoreCard = LayoutInflater.from(context).inflate(R.layout.see_more_card, container, false)
        seeMoreCard.setOnClickListener {
            Log.d("ListsFragment", "SeeMoreCard tıklandı, listDto id: ${listDto.id}")
            onSeeMoreClicked(listDto)
        }
        container.addView(seeMoreCard)
    }

    private val onSeeMoreClicked: (ListDto) -> Unit = { listDto ->
        val action = HomePageFragmentDirections
            .actionHomePageFragmentToThreeColumnFragment(
                title = listDto.title ?: "List",
                listId = listDto.id,
                type = null,
                userId = 0
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
