package com.example.frontendbook.ui.profile

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.frontendbook.R
import com.example.frontendbook.databinding.BottomSheetChooseProfileImageBinding


// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ChooseProfileImageBottomSheetFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class ChooseProfileImageBottomSheetFragment(
    private val onImageSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_choose_profile_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.imgAvatar1).setOnClickListener {
            onImageSelected(R.drawable.bookworms)
            dismiss()
        }
        view.findViewById<ImageView>(R.id.imgAvatar2).setOnClickListener {
            onImageSelected(R.drawable.bookfriends)
            dismiss()
        }
        view.findViewById<ImageView>(R.id.imgAvatar3).setOnClickListener {
            onImageSelected(R.drawable.bookbibliofil)
            dismiss()
        }
        view.findViewById<ImageView>(R.id.imgAvatar4).setOnClickListener {
            onImageSelected(R.drawable.bookcat)
            dismiss()
        }
    }
}
