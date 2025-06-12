package com.example.frontendbook.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.frontendbook.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePasswordBottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_change_password_fragment, container, false)
    }
}
