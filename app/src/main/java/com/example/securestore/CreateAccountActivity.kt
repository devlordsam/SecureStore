package com.example.securestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {
//sam
    private lateinit var edtUsername: EditText
    private lateinit var edtPass1: EditText
    private lateinit var edtPass2: EditText
    private lateinit var btnCreate: Button
    private lateinit var btnLogin: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mAuth = FirebaseAuth.getInstance()

        edtUsername = findViewById(R.id.editTextCAUsername)
        edtPass1 = findViewById(R.id.editTextCAPass1)
        edtPass2 = findViewById(R.id.editTextCAPass2)
        btnCreate = findViewById(R.id.buttonCA)
        btnLogin = findViewById(R.id.buttonCALogin)

        btnCreate.setOnClickListener {
            validateData()
        }

        btnLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateData(){

        if (edtUsername.text.toString().isEmpty()){
            Toast.makeText(this, "Please Fill Username!", Toast.LENGTH_SHORT).show()
            edtUsername.requestFocus()
        }
        else if (edtPass1.text.toString().isEmpty()){
            Toast.makeText(this, "Please Fill Password!", Toast.LENGTH_SHORT).show()
            edtPass1.requestFocus()
        }
        else if (edtPass2.text.toString().isEmpty()){
            Toast.makeText(this, "Please Re-enter Password!", Toast.LENGTH_SHORT).show()
            edtPass2.requestFocus()
        }
        else if (edtPass1.text.toString().trim().length < 8 || edtPass2.text.toString().trim().length < 8){
            Toast.makeText(this, "Password Too Small!", Toast.LENGTH_SHORT).show()
        }
        else if (edtPass1.text.toString().trim() != edtPass2.text.toString().trim()){
            Toast.makeText(this, "Password Not Same!", Toast.LENGTH_SHORT).show()
        }
        else{
            createAccount(edtUsername.text.toString().trim(), edtPass1.text.toString().trim())
        }
    }

    private fun createAccount(email :String, pass :String){

        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->

                if (task.isSuccessful){

                    val currentUser = mAuth.currentUser!!

                    currentUser.sendEmailVerification().addOnCompleteListener { task2 ->

                        if (task2.isSuccessful){
                            Toast.makeText(this, "Verification Email Sent!", Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Network Error!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}