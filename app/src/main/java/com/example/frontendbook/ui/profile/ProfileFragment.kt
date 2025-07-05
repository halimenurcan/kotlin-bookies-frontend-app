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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.FragmentProfileBinding
import com.example.frontendbook.domain.model.UserListType

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel
    private val TAG = "ProfileFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView() çağrıldı")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() çağrıldı")

        // ViewModel oluşturma
        viewModel = ViewModelProvider(this, UserViewModelFactory(requireContext()))
            .get(UserViewModel::class.java)
        Log.d(TAG, "ViewModel örneklendi")

        // LiveData gözlemleri
        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d(TAG, "observe(user) -> $user")
            binding.usernameText.text = user.username
            if (!user.profileImageUrl.isNullOrBlank()) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.avatar)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.avatar)
            }
        }
        viewModel.followersCount.observe(viewLifecycleOwner) { count ->
            Log.d(TAG, "observe(followersCount) -> $count")
            binding.btnFollowers.text = getString(R.string.followers_count, count)
        }
        viewModel.followingCount.observe(viewLifecycleOwner) { count ->
            Log.d(TAG, "observe(followingCount) -> $count")
            binding.btnFollowing.text = getString(R.string.following_count, count)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Log.e(TAG, "observe(error) -> $msg")
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        // Animasyon
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

        // SharedPrefs'ten userId
        val prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        Log.d(TAG, "SharedPrefs user_id = $userId")
        if (userId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // Veri yükle
        viewModel.loadUser(userId)

        // Buton click listener’ları
        binding.profileImage.setOnClickListener {
            Log.d(TAG, "profileImage clicked")
            ChooseProfileImageBottomSheetFragment { selectedResId ->
                Log.d(TAG, "Yeni avatar seçildi: $selectedResId")
                binding.profileImage.setImageResource(selectedResId)
            }.show(parentFragmentManager, "ChooseProfile")
        }
        binding.btnSettings.setOnClickListener {
            SettingsBottomSheetFragment().show(parentFragmentManager, "Settings")
        }

        binding.btnRead.setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Read")
                putString("arg_type",  "read")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        binding.btnReadlist.setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Readlist")
                putString("arg_type",  "readlist")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        binding.btnLists.setOnClickListener {
            findNavController().navigate(R.id.listsFragment)
        }
        binding.btnLikes.setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Likes")
                putString("arg_type",  "likes")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }
        binding.btnFollowers.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }
        binding.btnFollowing.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWING)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }
    }

    // ... diğer butonlar için de benzer Log ekleyebilirsiniz ...

override fun onDestroyView() {
    _binding = null
    super.onDestroyView()
}}


