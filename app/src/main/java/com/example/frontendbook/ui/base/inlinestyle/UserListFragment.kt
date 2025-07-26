package com.example.frontendbook.ui.base.inlinestyle

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.data.model.toSimple
import com.example.frontendbook.databinding.FragmentUserListBinding
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.ui.base.adapter.UserListAdapter
import com.example.frontendbook.ui.profile.FollowerViewModel
import com.example.frontendbook.ui.profile.FollowerViewModelFactory

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listType: UserListType
    private var profileUserId: Long = -1L

    private lateinit var viewModel: FollowerViewModel
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listType = it.getSerializable("userListType") as UserListType
            profileUserId = it.getLong("profileUserId")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = UserListAdapter(emptyList()) { selectedUser ->
            val bundle = Bundle().apply {
                putLong("userId", selectedUser.userId.toLong())
            }
            findNavController().navigate(R.id.otherUserProfileFragment, bundle)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        viewModel = ViewModelProvider(
            this,
            FollowerViewModelFactory(requireContext())
        ).get(FollowerViewModel::class.java)


        when (listType) {
            UserListType.FOLLOWERS -> {
                viewModel.loadFollowers(profileUserId)
                viewModel.followers.observe(viewLifecycleOwner) { users ->
                    adapter.submitList(users.map{it.toSimple()})
                }
            }
            UserListType.FOLLOWING -> {
                viewModel.loadFollowing(profileUserId)
                viewModel.following.observe(viewLifecycleOwner) { users ->
                    adapter.submitList(users.map{it.toSimple()})
                }
            }
        }


        viewModel.error.observe(viewLifecycleOwner) { err ->
            err?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }


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
