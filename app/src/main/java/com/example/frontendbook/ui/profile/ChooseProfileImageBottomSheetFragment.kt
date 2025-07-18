package com.example.frontendbook.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.frontendbook.R

class ChooseProfileImageBottomSheetFragment(
    private val onImageSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_choose_profile_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel'i factory ile oluştur
        val factory = UserViewModelFactory(requireContext())
        viewModel = ViewModelProvider(requireActivity(), factory)[UserViewModel::class.java]

        // SharedPreferences başlat
        sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedAvatarId = sharedPrefs.getInt("user_avatar", R.drawable.avatar)

        // Avatar eşlemeleri
        val avatarMap = mapOf(
            R.id.imgAvatar1 to Pair(R.drawable.bookworms, R.id.imgCheck1),
            R.id.imgAvatar2 to Pair(R.drawable.bookfriends, R.id.imgCheck2),
            R.id.imgAvatar3 to Pair(R.drawable.bookbibliofil, R.id.imgCheck3),
            R.id.imgAvatar4 to Pair(R.drawable.bookcat, R.id.imgCheck4)
        )

        // Başlangıçta tik işaretini göster
        for ((_, pair) in avatarMap) {
            val checkImageView = view.findViewById<ImageView>(pair.second)
            checkImageView.visibility = if (pair.first == savedAvatarId) View.VISIBLE else View.GONE
        }

        // Avatar tıklama işlemleri
        for ((avatarViewId, pair) in avatarMap) {
            val avatarImageView = view.findViewById<ImageView>(avatarViewId)
            val checkImageView = view.findViewById<ImageView>(pair.second)

            avatarImageView.setOnClickListener {
                // Tüm tikleri gizle
                avatarMap.values.forEach { (_, checkId) ->
                    view.findViewById<ImageView>(checkId).visibility = View.GONE
                }

                // Seçilen tik'i göster
                checkImageView.visibility = View.VISIBLE

                // Seçimi kaydet
                sharedPrefs.edit().putInt("user_avatar", pair.first).apply()

                // Seçilen avatar'ın backend id'sini belirle (Long)
                val avatar: Long = when (pair.first) {
                    R.drawable.bookworms -> 1L
                    R.drawable.bookfriends -> 2L
                    R.drawable.bookbibliofil -> 3L
                    R.drawable.bookcat -> 4L
                    else -> 0L
                }

                // Kullanıcı ID'sini al
                val userId: Long = sharedPrefs.getLong("user_id", -1L)
                Log.d("ChooseProfile", "Avatar seçildi → userId=$userId, avatarId=$avatar")

                // Geçerliyse API'ye gönder
                if (userId != -1L && avatar != 0L) {
                    viewModel.updateAvatar(userId, avatar)

                }

                // 300ms sonra callback ve kapatma
                view.postDelayed({
                    onImageSelected(pair.first)
                    dismiss()
                }, 300)
            }
        }
    }
}
