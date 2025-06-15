package com.example.frontendbook.ui.profile

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.frontendbook.R

class ChooseProfileImageBottomSheetFragment(
    private val onImageSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var sharedPrefs: android.content.SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_choose_profile_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedAvatarId = sharedPrefs.getInt("user_avatar", R.drawable.avatar)

        val avatarMap = mapOf(
            R.id.imgAvatar1 to Pair(R.drawable.bookworms, R.id.imgCheck1),
            R.id.imgAvatar2 to Pair(R.drawable.bookfriends, R.id.imgCheck2),
            R.id.imgAvatar3 to Pair(R.drawable.bookbibliofil, R.id.imgCheck3),
            R.id.imgAvatar4 to Pair(R.drawable.bookcat, R.id.imgCheck4)
        )

        // --- Başlangıçta kaydedilmiş tik'i göster ---
        for ((avatarViewId, pair) in avatarMap) {
            val checkImageView = view.findViewById<ImageView>(pair.second)
            checkImageView.visibility = if (pair.first == savedAvatarId) View.VISIBLE else View.GONE
        }

        // --- Avatar seçildiğinde ---
        for ((avatarViewId, pair) in avatarMap) {
            val avatarImageView = view.findViewById<ImageView>(avatarViewId)
            val checkImageView = view.findViewById<ImageView>(pair.second)

            avatarImageView.setOnClickListener {
                // Tüm tikleri gizle
                avatarMap.values.forEach { (_, checkId) ->
                    view.findViewById<ImageView>(checkId).visibility = View.GONE
                }

                // Seçilenin tik'ini göster
                checkImageView.visibility = View.VISIBLE

                // Seçimi kaydet
                sharedPrefs.edit().putInt("user_avatar", pair.first).apply()

                // 300ms sonra geri bildir
                view.postDelayed({
                    onImageSelected(pair.first)
                    dismiss()
                }, 300)
            }
        }
    }
}
