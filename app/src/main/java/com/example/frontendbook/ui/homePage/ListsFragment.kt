package com.example.frontendbook.ui.homePage

import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.databinding.FragmentListsBinding
import com.example.frontendbook.ui.base.adapter.ListAdapter
import com.example.frontendbook.ui.base.adapter.OtherListAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ListsFragment : Fragment() {
    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListsViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var otherListAdapter: OtherListAdapter

    private var showUserLists: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUserLists = arguments?.getBoolean("showUserLists", true) ?: true
        binding.btnAddList.visibility = if (showUserLists) View.VISIBLE else View.GONE

        viewModel = ViewModelProvider(
            this,
            ListsViewModelFactory(requireContext())
        )[ListsViewModel::class.java]

        // --- ADAPTER TANIMLARI ---
        listAdapter = ListAdapter(
            emptyList(),
            onClick = { list ->
                // NAVIGATION ARGS DÜZGÜN GÖNDER!
                val bundle = Bundle().apply {
                    putString("title", list.title ?: "List")
                    putLong("listId", list.id)
                    putString("type", null) // veya başka bir tip göndereceksen burada belirt
                    // İstersen userId da gönderebilirsin (örn. kendi listeleri ise)
                }
                findNavController().navigate(R.id.actionListsFragmentToThreeColumnFragment, bundle)
            },
            onDeleteClick = { listId ->
                viewModel.deleteList(listId)
            }
        )

        otherListAdapter = OtherListAdapter(
            lists = emptyList(),
            onFollowClick = { listDto ->
                Toast.makeText(requireContext(), "Followed ${listDto.title}", Toast.LENGTH_SHORT).show()
            },
            onBookClick = { bookId ->
                Toast.makeText(requireContext(), "Clicked bookId: $bookId", Toast.LENGTH_SHORT).show()
            }
        )

        // ADAPTER ve LAYOUT
        binding.listsRecyclerView.adapter = if (showUserLists) listAdapter else otherListAdapter
        binding.listsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAddList.setOnClickListener {
            if (showUserLists) showAddListDialog()
        }

        // Menü
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.lists_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_list -> {
                        if (showUserLists) showAddListDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // SharedPreferences ile userId al
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)

        // Gözlemleme ve veri yükleme
        if (showUserLists && userId != -1L) {
            viewModel.loadUserLists(userId)
            viewModel.lists.observe(viewLifecycleOwner) { lists ->
                listAdapter.submitList(lists)
                populateBookCards(lists)
            }
        } else {
            viewModel.loadExploreLists()
            viewModel.lists.observe(viewLifecycleOwner) { lists ->
                otherListAdapter.submitList(lists)
                populateBookCards(lists)
            }
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
                itemView?.let {
                    val bookContainer = it.findViewById<LinearLayout>(R.id.bookContainer)
                    populateBooks(listDto, bookContainer)
                }
            }
        }
    }

    private fun populateBooks(listDto: ListDto, container: LinearLayout) {
        container.removeViews(1, container.childCount - 2)
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
            container.addView(bookView, container.childCount - 1)
        }
        val seeMoreCard = container.findViewById<View>(R.id.seeMoreCard)
        seeMoreCard.setOnClickListener {
            Log.d("ListsFragment", "SeeMoreCard tıklandı, listDto id: ${listDto.id}")
            onSeeMoreClicked(listDto)
        }
    }

    // SafeArgs ile navigation yönlendirmesi (TAVSİYE EDİLEN YOL)
    private val onSeeMoreClicked: (ListDto) -> Unit = { listDto ->
        Log.d("ListsFragment", "onSeeMoreClicked BAŞLANGIÇ, id: ${listDto.id}")

        // Eğer SafeArgs kullanıyorsan:
        val action = ListsFragmentDirections.actionListsFragmentToThreeColumnFragment(
            title = listDto.title ?: "List",
            listId = listDto.id,
            type = null,
            userId = 0 // istersen kullanıcı id de gönderebilirsin (nav_graph.xml'de varsa)
        )
        findNavController().navigate(action)
        Log.d("ListsFragment", "onSeeMoreClicked BİTİŞ")
    }

    private fun showAddListDialog() {
        val context = requireContext()
        val inputLayout = TextInputLayout(context)
        val editText = TextInputEditText(context)

        inputLayout.hint = "List Name"
        inputLayout.setPadding(50, 0, 50, 0)
        editText.setSingleLine()
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
                        Toast.makeText(context, "Liste oluşturuldu", Toast.LENGTH_SHORT).show()
                        viewModel.loadUserLists(userId)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Liste oluşturulamadı", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                inputLayout.error = "Liste adı boş olamaz"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(showUserLists: Boolean): ListsFragment {
            val fragment = ListsFragment()
            fragment.arguments = Bundle().apply {
                putBoolean("showUserLists", showUserLists)
            }
            return fragment
        }
    }
}
