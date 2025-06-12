package com.example.frontendbook.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.frontendbook.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy veriler (örnek amaçlı). Gerçek uygulamada ViewModel veya başka bir kaynaktan gelir.
        val username = "100Lesh"
        val name = "John"
        val surname = "Doe"
        val email = "john.doe@example.com"

        view.findViewById<EditText>(R.id.settingsUsername).setText(username)
        view.findViewById<EditText>(R.id.settingsName).setText(name)
        view.findViewById<EditText>(R.id.settingsSurname).setText(surname)
        view.findViewById<EditText>(R.id.settingsEmail).setText(email)

        view.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }


        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            // TODO: Save işlemi yapılacak
            dismiss()
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            // TODO: Çıkış işlemi
            dismiss()
        }

        view.findViewById<Button>(R.id.btnChangePassword).setOnClickListener {
            // TODO: Şifre değiştirme Bottom Sheet aç
        }
    }
}
