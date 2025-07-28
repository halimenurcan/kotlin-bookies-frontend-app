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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddList.visibility = View.GONE

        viewModel = ViewModelProvider(this, ListsViewModelFactory(requireContext()))
            .get(ListsViewModel::class.java)

        val repo = ListFollowsRepository(RetrofitClient.listFollowsApiService(requireContext()))
        followViewModel = ListFollowViewModel(repo)

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val currentUserId = prefs.getLong("user_id", -1L)
        val targetUserId = arguments?.getLong("arg_user_id", -1L) ?: -1L

        otherListAdapter = OtherListAdapter(
            lists = emptyList(),
            onFollowClick = { listDto ->
                if (currentUserId != -1L) {
                    followViewModel.toggleFollow(currentUserId, listDto.id)
                } else {
                    Toast.makeText(requireContext(), "User not found!", Toast.LENGTH_SHORT).show()
                }
            },
            onBookClick = { book ->
                val domainBook = Book(
                    id = book.id,
                    title = book.title.orEmpty(),
                    isbn = book.isbn.orEmpty(),
                    coverImageUrl = book.coverImageUrl.orEmpty(),
                    author = book.author?.toString().orEmpty(),
                    description = book.description.orEmpty(),
                    pageCount = book.pageCount ?: 0,
                    publisher = book.publisher.orEmpty(),
                    publishedYear = book.publishedYear ?: 0,
                    rating = book.rating ?: 0
                )
                val action = OtherListsFragmentDirections
                    .actionOtherListsFragmentToBookDetailFragment(domainBook)
                findNavController().navigate(action)
            },
            onSeeMoreClick = { listDto ->
                val bundle = Bundle().apply {
                    putString("title", listDto.title ?: "List")
                    putLong("listId", listDto.id)
                    putString("type", "custom")
                    putLong("userId", listDto.owner.id)
                }
                findNavController().navigate(R.id.action_global_threeColumnFragment, bundle)
            }
        )

        binding.listsRecyclerView.apply {
            adapter = otherListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        if (targetUserId != -1L) {
            viewModel.loadOtherListsForUser(targetUserId)
        } else {
            viewModel.loadOtherLists()
        }

        if (currentUserId != -1L) {
            followViewModel.loadFollowedListIds(currentUserId)
        }

        followViewModel.followedListIds.observe(viewLifecycleOwner) { followedIds ->
            otherListAdapter.setFollowedListIds(followedIds)
            otherListAdapter.notifyDataSetChanged()
        }

        viewModel.otherLists.observe(viewLifecycleOwner) { lists ->
            otherListAdapter.submitList(lists)
            populateBookCards(lists)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()
            }
        }

        followViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), "Follow error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateBookCards(lists: List<ListDto>) {
        binding.listsRecyclerView.post {
            lists.forEachIndexed { index, listDto ->
                val itemView = binding.listsRecyclerView.layoutManager?.findViewByPosition(index)
                val bookContainer = itemView?.findViewById<LinearLayout>(R.id.bookContainer)

                if (bookContainer != null) {
                    populateBooks(listDto, bookContainer)
                }
            }
        }
    }

    private fun populateBooks(listDto: ListDto, container: LinearLayout) {
        container.removeAllViews()
        val inflater = LayoutInflater.from(context)

        listDto.books.forEach { book ->
            val bookView = inflater.inflate(R.layout.item_book_grid, container, false)
            val bookImage = bookView.findViewById<ImageView>(R.id.bookImage)
            val bookTitle = bookView.findViewById<TextView>(R.id.bookTitle)

            bookTitle.text = book.title.orEmpty()

            if (!book.coverImageUrl.isNullOrBlank()) {
                Glide.with(requireContext())
                    .load(book.coverImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(bookImage)
            } else {
                bookImage.setImageResource(R.drawable.placeholder)
            }

            bookView.setOnClickListener {
                val domainBook = Book(
                    id = book.id,
                    title = book.title.orEmpty(),
                    isbn = book.isbn.orEmpty(),
                    coverImageUrl = book.coverImageUrl.orEmpty(),
                    author = book.author?.toString().orEmpty(),
                    description = book.description.orEmpty(),
                    pageCount = book.pageCount ?: 0,
                    publisher = book.publisher.orEmpty(),
                    publishedYear = book.publishedYear ?: 0,
                    rating = book.rating ?: 0
                )
                val action = OtherListsFragmentDirections
                    .actionOtherListsFragmentToBookDetailFragment(domainBook)
                findNavController().navigate(action)
            }

            container.addView(bookView)
        }

        val seeMoreCard = inflater.inflate(R.layout.see_more_card, container, false)
        seeMoreCard.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", listDto.title ?: "List")
                putLong("listId", listDto.id)
                putString("type", "custom")
                putLong("userId", listDto.owner.id)
            }
            findNavController().navigate(R.id.action_global_threeColumnFragment, bundle)
        }
        container.addView(seeMoreCard)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
