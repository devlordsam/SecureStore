package com.lordsam.securestore

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.AccountData

class LoginActivity : AppCompatActivity() {
//sam
    private lateinit var edtUsername: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPref = getSharedPreferences("CreateAccount", MODE_PRIVATE)

        edtUsername = findViewById(R.id.editTextLoginUsername)
        edtPass = findViewById(R.id.editTextLoginPass)
        btnLogin = findViewById(R.id.buttonLogin)

        try {
            //sharedPreference :load data
            val gson = Gson()
            val json = sharedPref.getString("accountData", "empty")
            val type  = object: TypeToken<AccountData>(){}.type
            val data = gson.fromJson<AccountData>(json, type)

            if (data == null) {
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
            edtUsername.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please Fill Username!", Toast.LENGTH_SHORT).show()
                edtUsername.requestFocus()
            }
            edtPass.text.toString().isEmpty() -> {
                Toast.makeText(this, "Please Fill Password!", Toast.LENGTH_SHORT).show()
                edtPass.requestFocus()
            }
            edtPass.text.toString().trim().length < 8 -> {
                Toast.makeText(this, "Password Too Small!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                login(edtUsername.text.toString().trim(), edtPass.text.toString().trim())
            }
        }
    }

    private fun login(userName: String, pass: String) {
        val gson = Gson()
        val json = sharedPref.getString("accountData", "empty")
        val type  = object: TypeToken<AccountData>(){}.type
        val data = gson.fromJson<AccountData>(json, type)

        val getUsername = data.email
        val getPass = data.pass

        if ((getUsername == userName) && (getPass == pass)){
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
        }
    }
}