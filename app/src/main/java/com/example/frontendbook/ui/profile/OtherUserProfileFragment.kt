package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentOtherUserProfileBinding
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.ui.base.threecolumn.ThreeColumnFragment

class OtherUserProfileFragment : Fragment() {

    private var _binding: FragmentOtherUserProfileBinding? = null
    private val binding get() = _binding!!

    private val args: OtherUserProfileFragmentArgs by navArgs()
    private val targetUserId: Long get() = args.userId

    private val currentUserId: Long by lazy {
        requireContext()
            .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            .getLong("user_id", -1L)
    }

    private val userViewModel: UserViewModel by viewModels { UserViewModelFactory(requireContext()) }
    private val followerViewModel: FollowerViewModel by viewModels { FollowerViewModelFactory(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUserId == -1L || targetUserId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bilgisi eksik", Toast.LENGTH_SHORT).show()
            return
        }

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.otherUsernameText.text = user.username
            if (!user.profileImageUrl.isNullOrBlank()) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.avatar)
                    .circleCrop()
                    .into(binding.otherProfileImage)
            } else {
                binding.otherProfileImage.setImageResource(R.drawable.avatar)
            }
        }
        userViewModel.error.observe(viewLifecycleOwner) { it?.let { m -> Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show() } }

        followerViewModel.isFollowing.observe(viewLifecycleOwner) { following ->
            binding.btnFollowAction.text = if (following) getString(R.string.unfollow) else getString(R.string.follow)
        }
        followerViewModel.error.observe(viewLifecycleOwner) { it?.let { m -> Toast.makeText(requireContext(), m, Toast.LENGTH_LONG).show() } }

        userViewModel.loadUser(targetUserId)
        // *DİKKAT: Doğru ID SIRASI*
        followerViewModel.loadFollowStatus(targetUserId, currentUserId)

        binding.btnFollowAction.setOnClickListener {
            followerViewModel.toggleFollow(targetUserId, currentUserId)
        }

        // Manuel bundle + navigate:
        binding.btnOtherFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
                    putLong("arg_profile_user_id", targetUserId)
                }
            )
        }
        binding.btnOtherFollowing.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("arg_user_list_type", UserListType.FOLLOWING)
                    putLong("arg_profile_user_id", targetUserId)
                }
            )
        }
        binding.btnOtherRead.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Read")
                putString(ThreeColumnFragment.ARG_TYPE, "read")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        binding.btnOtherReadlist.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Readlist")
                putString(ThreeColumnFragment.ARG_TYPE, "readlist")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        binding.btnOtherLists.setOnClickListener {
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("arg_title", "List")
                    putString("arg_type", "List")
                    putLong(ThreeColumnFragment.ARG_LIST_ID, targetUserId)
                }
            )
        }
        binding.btnOtherLikes.setOnClickListener {
            val bundle = Bundle().apply {
                putString(ThreeColumnFragment.ARG_TITLE, "Likes")
                putString(ThreeColumnFragment.ARG_TYPE, "likes")
                putLong(ThreeColumnFragment.ARG_USER_ID, targetUserId)
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
        }
}