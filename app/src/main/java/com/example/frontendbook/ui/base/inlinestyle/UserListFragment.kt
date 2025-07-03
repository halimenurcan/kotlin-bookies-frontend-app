package com.example.frontendbook.ui.base.inlinestyle

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.example.frontendbook.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentUserListBinding
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.ui.base.adapter.UserListAdapter

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listType: UserListType
    private var profileUserId: Long = -1L

    private lateinit var viewModel: UserListViewModel
    private lateinit var adapter: UserListAdapter

    companion object {
        private const val ARG_TYPE = "arg_user_list_type"
        private const val ARG_PROFILE_USER_ID = "arg_profile_user_id"

        /**
         * Kullanırken:
         * UserListFragment.newInstance(UserListType.FOLLOWERS, someUserId)
         */
        fun newInstance(type: UserListType, profileUserId: Long): UserListFragment =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TYPE, type)
                    putLong(ARG_PROFILE_USER_ID, profileUserId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listType = it.getSerializable(ARG_TYPE) as UserListType
            profileUserId = it.getLong(ARG_PROFILE_USER_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1) RecyclerView + Adapter
        adapter = UserListAdapter(emptyList()) { selectedUser ->
            // Tıklanan kullanıcının profil sayfasına git
            val bundle = Bundle().apply {
                putString("user_id", selectedUser.userId)
            }
            findNavController().navigate(R.id.otherUserProfileFragment, bundle)

        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 2) ViewModel
        viewModel = ViewModelProvider(
            this,
            UserListViewModelFactory(requireContext())
        ).get(UserListViewModel::class.java)

        // 3) Veri yükle
        viewModel.loadList(profileUserId, listType)

        // 4) Gözle
        viewModel.users.observe(viewLifecycleOwner) { users ->
            adapter.submitList(users)
        }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        // 5) Başlık
        binding.headerTitle.text = when (listType) {
            UserListType.FOLLOWERS -> getString(R.string.title_followers)
            UserListType.FOLLOWING -> getString(R.string.title_following)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
