package com.lordsam.securestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HelpActivity : AppCompatActivity() {

    private lateinit var btnResetPass: Button
    private lateinit var btnResetUsername: Button
    private lateinit var btnForgotPass: Button
    private lateinit var btnForgotUsername: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        btnResetPass = findViewById(R.id.buttonHelpResetPassword)
        btnResetUsername = findViewById(R.id.buttonHelpResetUsername)
        btnForgotPass = findViewById(R.id.buttonHelpForgotPassword)
        btnForgotUsername = findViewById(R.id.buttonHelpForgotUsername)


        btnResetPass.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
        btnResetUsername.setOnClickListener {
            startActivity(Intent(this, ResetUsernameActivity::class.java))
        }
        btnForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        btnForgotUsername.setOnClickListener {
            startActivity(Intent(this, ForgotUsernameActivity::class.java))
        }
    }
}