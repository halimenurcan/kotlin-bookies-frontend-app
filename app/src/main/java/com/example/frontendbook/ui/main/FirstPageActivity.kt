package com.example.frontendbook

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.frontendbook.ui.register.RegisterActivity
import com.example.frontendbook.ui.signIn.SignInActivity

import com.google.android.material.button.MaterialButton

class FirstPageActivity : AppCompatActivity() {
    lateinit var layout : ConstraintLayout
    lateinit var signInButton : MaterialButton
    lateinit var createAcountButton : MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_firstpage)

        createAcountButton = findViewById(R.id.createaccount)
        signInButton = findViewById(R.id.signin)
        // Arkaplan rengini ayarla
        layout = findViewById(R.id.main)
        layout.setBackgroundColor(Color.parseColor("#FAF7F2"))


        signInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        createAcountButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
