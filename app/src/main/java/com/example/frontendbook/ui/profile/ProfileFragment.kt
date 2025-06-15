package com.example.frontendbook.ui.profile

import android.view.animation.AnimationUtils
import android.os.Handler

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.frontendbook.R
import com.example.frontendbook.domain.model.UserListType

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Kullanıcı adı gösterimi
        val username = sharedPrefs.getString("user_username", "default")
        view.findViewById<TextView>(R.id.usernameText).text = username

        // Kayıtlı avatar varsa göster
        val savedAvatar = sharedPrefs.getInt("user_avatar", R.drawable.avatar)
        val profileImageView = view.findViewById<ImageView>(R.id.profileImage)
        profileImageView.setImageResource(savedAvatar)

        // Avatar'a tıklayınca bottom sheet aç
        profileImageView.setOnClickListener {
            // BottomSheet açılır
            ChooseProfileImageBottomSheetFragment { selectedImageResId ->
                // Kullanıcı bir avatar seçtiğinde animasyon uygulanır
                val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
                val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)

                profileImageView.startAnimation(fadeOut)

                Handler(Looper.getMainLooper()).postDelayed({
                    profileImageView.setImageResource(selectedImageResId)
                    profileImageView.startAnimation(fadeIn)
                    sharedPrefs.edit().putInt("user_avatar", selectedImageResId).apply()
                }, 400)
            }.show(parentFragmentManager, "ChooseProfileImageBottomSheet")
        }


        // Ayarlar BottomSheet
        view.findViewById<View>(R.id.btnSettings).setOnClickListener {
            SettingsBottomSheetFragment().show(parentFragmentManager, "SettingsBottomSheet")
        }

        // Read
        view.findViewById<View>(R.id.btnRead).setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Read")
                putString("arg_type", "read")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        // Readlist
        view.findViewById<View>(R.id.btnReadlist).setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Readlist")
                putString("arg_type", "readlist")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        // Lists
        view.findViewById<View>(R.id.btnLists).setOnClickListener {
            findNavController().navigate(R.id.listsFragment)
        }

        // Likes
        view.findViewById<View>(R.id.btnLikes).setOnClickListener {
            val bundle = Bundle().apply {
                putString("arg_title", "Likes")
                putString("arg_type", "likes")
            }
            findNavController().navigate(R.id.threeColumnFragment, bundle)
        }

        // Followers
        view.findViewById<View>(R.id.btnFollowers).setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWERS)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        // Following
        view.findViewById<View>(R.id.btnFollowing).setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("arg_user_list_type", UserListType.FOLLOWING)
            }
            findNavController().navigate(R.id.userListFragment, bundle)
        }

        return view
    }
}
