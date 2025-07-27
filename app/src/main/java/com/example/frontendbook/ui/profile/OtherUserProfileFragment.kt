package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.frontendbook.R
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.UserRepository
import com.example.frontendbook.databinding.FragmentOtherUserProfileBinding
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.ui.base.threecolumn.ThreeColumnFragment
import kotlinx.coroutines.launch

class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRepository: UserRepository

    private var targetUserId: Long = -1L
    private val currentUserId: Long by lazy {
        requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)
    }

    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(requireContext()) }
    private val followerViewModel: FollowerViewModel by viewModels { FollowerViewModelFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetUserId = arguments?.getLong("userId", -1L) ?: -1L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUserId == -1L || targetUserId == -1L) {
            Toast.makeText(requireContext(), "User information is missing", Toast.LENGTH_SHORT).show()
            return
        }

        userRepository = UserRepository(RetrofitClient.userApiService(requireContext()))
        loadAvatar(targetUserId)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.otherUsernameText.text = user.username
        }

        userViewModel.error.observe(viewLifecycleOwner) {
            it?.let { m -> Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show() }
        }

        userViewModel.followersCount.observe(viewLifecycleOwner) { count ->
            binding.btnOtherFollowers.text = getString(R.string.following_count, count)
        }

        userViewModel.followingCount.observe(viewLifecycleOwner) { count ->
            binding.btnOtherFollowing.text = getString(R.string.followers_count, count)
        }

        followerViewModel.isFollowing.observe(viewLifecycleOwner) { following ->
            binding.btnFollowAction.text = if (following) getString(R.string.unfollow) else getString(R.string.follow)
        }

        followerViewModel.loadIsFollowing(targetUserId, currentUserId)
        userViewModel.loadUser(targetUserId)
        followerViewModel.loadFollowStatus(currentUserId, targetUserId)

        binding.btnFollowAction.setOnClickListener {
            followerViewModel.toggleFollow(currentUserId, targetUserId)
        }

        binding.btnOtherFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("userListType", UserListType.FOLLOWING)
                    putLong("profileUserId", targetUserId)
                }
            )
        }

        binding.btnOtherFollowing.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("userListType", UserListType.FOLLOWERS)
                    putLong("profileUserId", targetUserId)
                }
            )
        }

        binding.btnOtherRead.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Read")
                putString(ThreeColumnFragment.ARG_TYPE, "read")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
                putLong("listId", 0L)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        binding.btnOtherReadlist.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Readlist")
                putString(ThreeColumnFragment.ARG_TYPE, "readlist")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
                putLong("listId", 0L)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        binding.btnOtherLists.setOnClickListener {
            findNavController().navigate(
                R.id.otherListsFragment,
                Bundle().apply {
                    putLong("arg_user_id", targetUserId)
                }
            )
        }

        binding.btnOtherLikes.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Likes")
                putString(ThreeColumnFragment.ARG_TYPE, "likes")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
                putLong("listId", 0L)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
    }

    private fun loadAvatar(userId: Long) {
        lifecycleScope.launch {
            val avatarResponse = userRepository.fetchAvatar(userId)
            val avatarName = avatarResponse?.avatar ?: "avatar.png"
            val avatarDrawable = when (avatarName) {
                "bookworms.png"     -> R.drawable.bookworms
                "bookfriends.png"   -> R.drawable.bookfriends
                "bookbibliofil.png" -> R.drawable.bookbibliofil
                "bookcat.png"       -> R.drawable.bookcat
                else                -> R.drawable.avatar
            }
            binding.otherProfileImage.setImageResource(avatarDrawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
