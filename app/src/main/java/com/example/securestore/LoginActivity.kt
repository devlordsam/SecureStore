package com.example.securestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
//sam
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var mAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.editTextLoginEmail)
        edtPass = findViewById(R.id.editTextLoginPass)
        btnLogin = findViewById(R.id.buttonLogin)

        try {
            user = mAuth.currentUser!!
            if (user == null) {
                startActivity(Intent(this, CreateAccountActivity::class.java))
            }
        } catch (ex: Exception) {
            Log.i("catch", ex.stackTrace.toString())
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }


        btnLogin.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        when {
            edtEmail.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please Fill Username!", Toast.LENGTH_SHORT).show()
                edtEmail.requestFocus()
            }
            edtPass.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please Fill Password!", Toast.LENGTH_SHORT).show()
                edtPass.requestFocus()
            }
            edtPass.text.toString().trim().length < 8 -> {
                Toast.makeText(this, "Password Too Small!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                login(edtEmail.text.toString().trim(), edtPass.text.toString().trim())
            }
        }
    }

    private fun login(email: String, pass: String) {

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (user == null) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else if (user!!.isEmailVerified && user != null) {
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}