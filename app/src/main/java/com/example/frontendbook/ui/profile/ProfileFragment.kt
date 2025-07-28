package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.data.repository.UserRepository
import com.example.frontendbook.databinding.FragmentProfileBinding
import com.example.frontendbook.domain.model.UserListType
import com.example.frontendbook.ui.likedbooks.LikedBooksViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel
    private lateinit var readViewModel: ReadViewModel
    private lateinit var likedBooksViewModel: LikedBooksViewModel
    private lateinit var userRepository: UserRepository
    private val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val likedRepo = LikedBooksRepository(RetrofitClient.likedBooksApiService(requireContext()))
        likedBooksViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return LikedBooksViewModel(likedRepo) as T
                }
            }
        )[LikedBooksViewModel::class.java]

        viewModel = ViewModelProvider(this, UserViewModelFactory(requireContext()))
            .get(UserViewModel::class.java)

        readViewModel = ViewModelProvider(
            this,
            ReadViewModelFactory(requireContext())
        )[ReadViewModel::class.java]

        userRepository = UserRepository(RetrofitClient.userApiService(requireContext()))

        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        Log.d(TAG, "onViewCreated - userId from prefs = $userId")
        if (userId == -1L) {
            Toast.makeText(requireContext(), "User could not found", Toast.LENGTH_SHORT).show()
            return
        }


        loadAvatar(userId)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d(TAG, "observe(user) -> $user")
            binding.usernameText.text = user.username

            if (!user.profileImageUrl.isNullOrBlank()) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.avatar)
                    .into(binding.profileImage)
            }
        }
        viewModel.followingCount.observe(viewLifecycleOwner) { count ->
            binding.btnFollowers.text = getString(R.string.followers_count, count)
        }
        viewModel.followersCount.observe(viewLifecycleOwner) { count ->
            binding.btnFollowing.text = getString(R.string.following_count, count)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loadFollowersCount(userId)
        viewModel.loadFollowingCount(userId)

        // ReadViewModel LiveData gözlemleri
        readViewModel.toReadList.observe(viewLifecycleOwner) { list ->
            Log.d(TAG, "Books to read: ${list.size}")
        }
        readViewModel.readBooks.observe(viewLifecycleOwner) { list ->
            Log.d(TAG, "Books already read: ${list.size}")
        }
        readViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }


        val scaleAnim = ScaleAnimation(
            1f, 1.1f, 1f, 1.1f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 600
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.profileImage.startAnimation(scaleAnim)
        binding.usernameText.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_glow)
        )

        Log.d(TAG, "loadUser: $userId")
        viewModel.loadUser(userId)

        binding.profileImage.setOnClickListener {
            ChooseProfileImageBottomSheetFragment { selectedResId ->
                binding.profileImage.setImageResource(selectedResId)
                loadAvatar(userId)
            }.show(parentFragmentManager, "ChooseProfile")
        }

        binding.btnSettings.setOnClickListener {
            SettingsBottomSheetFragment().show(parentFragmentManager, "Settings")
        }

        binding.btnRead.setOnClickListener {
            readViewModel.loadReadBooks(userId)
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("title", "Read")
                    putString("type", "read")
                    putLong("userId", userId)
                    putLong("listId", 0)
                }
            )
        }

        binding.btnReadlist.setOnClickListener {
            readViewModel.loadToReadList(userId)
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("title", "Readlist")
                    putString("type", "readlist")
                    putLong("userId", userId)
                    putLong("listId", 0)
                }
            )
        }

        binding.btnLikes.setOnClickListener {
            likedBooksViewModel.loadLikedBooks(userId)
            findNavController().navigate(
                R.id.threeColumnFragment,
                Bundle().apply {
                    putString("title", "Likes")
                    putString("type", "likes")
                    putLong("userId", userId)
                    putLong("listId", 0)
                }
            )
        }
        // Takipçi ve Takip edilenler
        binding.btnFollowers.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("userListType", UserListType.FOLLOWERS)
                    putLong("profileUserId", userId)
                }
            )
        }
        binding.btnFollowing.setOnClickListener {
            findNavController().navigate(
                R.id.userListFragment,
                Bundle().apply {
                    putSerializable("userListType", UserListType.FOLLOWING)
                    putLong("profileUserId", userId)
                }
            )
        }
        binding.btnLists.setOnClickListener {
            val direction = ProfileFragmentDirections.actionProfileFragmentToListsFragment(
                argUserId = userId,
                argTitle = "Listelerim",
                argType = "custom",
                argListId = -1L,
                profileUserId = userId,
                showUserLists = true
            )
            findNavController().navigate(direction)
        }
    }

    private fun loadAvatar(userId: Long) {
        lifecycleScope.launch {
            val avatarResponse = userRepository.fetchAvatar(userId)
            val avatarName = avatarResponse?.avatar ?: "avatar.png"
            val avatarDrawable = when (avatarName) {
                "bookworms.png"    -> R.drawable.avatar
                "bookfriends.png"  -> R.drawable.bookbibliofil
                "bookbibliofil.png"-> R.drawable.bookworms
                "bookcat.png"      -> R.drawable.bookcat
                else               -> R.drawable.avatar
            }
            binding.profileImage.setImageResource(avatarDrawable)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
