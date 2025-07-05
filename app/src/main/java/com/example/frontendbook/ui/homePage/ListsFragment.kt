package com.example.frontendbook.ui.homePage

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentListsBinding
import com.example.frontendbook.ui.base.adapter.ExploreListAdapter
import com.example.frontendbook.ui.base.threecolumn.ThreeColumnFragment

class ListsFragment : Fragment() {

    private var _binding: FragmentListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListsViewModel

    private lateinit var listAdapter: ListAdapter
    private lateinit var exploreListAdapter: ExploreListAdapter

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
        ).get(ListsViewModel::class.java)

        if (showUserLists) {
            // Profil sayfası için adapter
            listAdapter = ListAdapter(emptyList()) { list ->
                val bundle = Bundle().apply { putLong("listId", list.id) }
                findNavController().navigate(R.id.action_listsFragment_to_listDetailFragment, bundle)
            }
            binding.listsRecyclerView.adapter = listAdapter
        } else {
            // Keşfet (homepage->lists) için adapter – SEE MORE tıklanınca yönlendir
            exploreListAdapter = ExploreListAdapter(emptyList()) { list ->
                // "See More" butonuna basınca ThreeColumnFragment'a yönlendir
                val fragment = ThreeColumnFragment.newInstance(
                    title = list.title ?: "List",
                    listId = list.id  // 👈 Liste ID’sini geçiriyoruz
                )

                parentFragmentManager.beginTransaction()
                    .replace(R.id.innerFragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            binding.listsRecyclerView.adapter = exploreListAdapter
        }

        binding.listsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAddList.setOnClickListener {
            if (showUserLists) showAddListDialog()
        }

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

        if (showUserLists) {
            val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)
            if (userId != -1L) {
                viewModel.loadUserLists(userId)
            } else {
                Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            }
        } else {
            viewModel.loadExploreLists()
        }

        viewModel.lists.observe(viewLifecycleOwner) { lists ->
            if (showUserLists) {
                listAdapter.submitList(lists)
            } else {
                exploreListAdapter.submitList(lists)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAddListDialog() {
        val context = requireContext()

        val inputLayout = com.google.android.material.textfield.TextInputLayout(context)
        val editText = com.google.android.material.textfield.TextInputEditText(context)

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
