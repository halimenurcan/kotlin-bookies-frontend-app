package com.example.frontendbook.ui.homePage

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.databinding.FragmentListsBinding
import com.example.frontendbook.ui.base.adapter.ListAdapter
import com.example.frontendbook.ui.base.adapter.OtherListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ListsFragment : Fragment() {
    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!
    private val args: ListsFragmentArgs by navArgs()

    private val listId: Long get() = args.argListId
    private val showUserLists: Boolean get() = args.showUserLists

    private lateinit var viewModel: ListsViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var otherListAdapter: OtherListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddList.visibility = if (showUserLists) View.VISIBLE else View.GONE

        viewModel = ViewModelProvider(
            this,
            ListsViewModelFactory(requireContext())
        )[ListsViewModel::class.java]

        listAdapter = ListAdapter(
            emptyList(),
            onClick = { list ->
                val bundle = Bundle().apply {
                    putString("title", list.title ?: "List")
                    putLong("listId", list.id)
                    putString("type", null)
                }
                findNavController().navigate(R.id.threeColumnFragment, bundle)
            },
            onDeleteClick = { viewModel.deleteList(it) }
        )

        otherListAdapter = OtherListAdapter(
            lists = emptyList(),
            onFollowClick = { listDto ->
                Toast.makeText(requireContext(), "Followed ${listDto.title}", Toast.LENGTH_SHORT).show()
            },
            onBookClick = { bookId ->
                Toast.makeText(requireContext(), "Clicked bookId: $bookId", Toast.LENGTH_SHORT).show()
            },
            onSeeMoreClick = onSeeMoreClicked
        )

        binding.listsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = if (showUserLists) listAdapter else otherListAdapter
        }

        binding.btnAddList.setOnClickListener {
            if (showUserLists) showAddListDialog()
        }

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)

        when {
            listId != -1L -> {
                Log.d("ListsFragment", "called from loadSingleList (listId=$listId)")
                viewModel.loadSingleList(listId)
            }
            showUserLists && userId != -1L -> {
                Log.d("ListsFragment", " called loadUserLists  (userId=$userId)")
                viewModel.loadUserLists(userId)
            }
            else -> {
                Log.d("ListsFragment", "called loadExploreLists ")
                viewModel.loadExploreLists()
            }
        }

        viewModel.lists.observe(viewLifecycleOwner) { lists ->
            Log.d("ListsFragment", " Observed list size: ${lists.size}")
            if (showUserLists || listId != -1L) {
                listAdapter.submitList(lists)
            } else {
                otherListAdapter.submitList(lists)
            }
            populateBookCards(lists)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun populateBookCards(lists: List<ListDto>) {
        binding.listsRecyclerView.post {
            lists.forEachIndexed { index, listDto ->
                val itemView = binding.listsRecyclerView.layoutManager?.findViewByPosition(index)
                itemView?.findViewById<LinearLayout>(R.id.bookContainer)?.let { container ->
                    populateBooks(listDto, container)
                }
            }
        }
    }

    private fun populateBooks(listDto: ListDto, container: LinearLayout) {
        val startIndex = 1
        val removableCount = container.childCount - 2
        if (removableCount > 0) {
            container.removeViews(startIndex, removableCount)
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
                val bundle = Bundle().apply { putLong("bookId", book.id) }
                findNavController().navigate(R.id.bookInfoPageFragment, bundle)
            }

            container.addView(bookView, container.childCount - 1)
        }

        container.findViewById<View>(R.id.seeMoreCard).setOnClickListener {
            onSeeMoreClicked(listDto)
        }
    }

    private val onSeeMoreClicked: (ListDto) -> Unit = { listDto ->
        val bundle = Bundle().apply {
            putString("title", listDto.title ?: "List")
            putLong("listId", listDto.id)
            putString("type", null)
            putLong("userId", 0)
        }
        findNavController().navigate(R.id.threeColumnFragment, bundle)
    }

    private fun showAddListDialog() {
        val context = requireContext()
        val inputLayout = TextInputLayout(context).apply {
            hint = "List Name"
            setPadding(50, 0, 50, 0)
        }

        val editText = TextInputEditText(context).apply {
            setSingleLine()
        }

        inputLayout.addView(editText)

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle("Create New List")
            .setView(inputLayout)
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
            ContextCompat.getColor(context, R.color.buttonSecondary)
        )
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
            ContextCompat.getColor(context, R.color.button_textPrimary)
        )

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val listName = editText.text.toString().trim()
            if (listName.isNotEmpty()) {
                val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val userId = prefs.getLong("user_id", -1L)
                viewModel.createList(userId, listName) { success ->
                    if (success) {
                        Toast.makeText(context, "The list has been created", Toast.LENGTH_SHORT).show()
                        viewModel.loadUserLists(userId)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Could not create list", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                inputLayout.error = "List name can not be empty!"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
