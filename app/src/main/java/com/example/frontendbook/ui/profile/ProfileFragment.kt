package com.example.frontendbook.ui.profile

import android.R.attr.repeatCount
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val profileImage = view.findViewById<ImageView>(R.id.profileImage)

        val scaleAnim = ScaleAnimation(
            1f, 1.1f, // X ekseni: %10 büyü
            1f, 1.1f, // Y ekseni: %10 büyü
            Animation.RELATIVE_TO_SELF, 0.5f, // Merkezden
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 600 // kısa sürede büyü
            fillAfter = false // eski haline dönsün
            interpolator = AccelerateDecelerateInterpolator()
        }

// Animasyonu başlat
        profileImage.startAnimation(scaleAnim)



        val usernameText = view.findViewById<TextView>(R.id.usernameText)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_glow)
        usernameText.startAnimation(animation)

        // 1. SharedPrefs'tan user_id al
        val prefs  = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = prefs.getLong("user_id", -1L)
        if (userId == -1L) {
            Toast.makeText(requireContext(), "Kullanıcı bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. ViewModel'i Factory ile oluştur
        viewModel = ViewModelProvider(this, UserViewModelFactory(requireContext()))
            .get(UserViewModel::class.java)

        // 3. Backend'den veriyi çek
        viewModel.loadUser(userId)

        // 4. LiveData gözlemleri
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.usernameText.text = user.username
            if (user.profileImageUrl != null) {
                Glide.with(this)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.avatar)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.avatar)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

        // 5. Mevcut avatar seçme, ayarlar ve navigasyon butonları
        binding.profileImage.setOnClickListener {
            ChooseProfileImageBottomSheetFragment { selectedResId ->
                binding.profileImage.setImageResource(selectedResId)
                // dilerseniz backend'e de güncelleme çağrısı yapabilirsiniz
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
