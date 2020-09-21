package com.lordsam.securestore

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lordsam.securestore.dataclass.AccountData

class CreateAccountActivity : AppCompatActivity() {
//sam

    private lateinit var edtUsername: EditText
    private lateinit var edtPass1: EditText
    private lateinit var edtPass2: EditText
    private lateinit var btnCreate: Button
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        sharedPref = getSharedPreferences("CreateAccount", MODE_PRIVATE)

        edtUsername = findViewById(R.id.editTextCAUsername)
        edtPass1 = findViewById(R.id.editTextCAPass1)
        edtPass2 = findViewById(R.id.editTextCAPass2)
        btnCreate = findViewById(R.id.buttonCA)

        btnCreate.setOnClickListener {
            validateData()
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

    private fun createAccount(userName :String, pass :String){

        //sharedPreference :add data
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(AccountData(userName, pass))
        editor.putString("accountData", json)
        editor.apply().also {
            if (sharedPref.getString("accountData", "empty") != "empty"){
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}