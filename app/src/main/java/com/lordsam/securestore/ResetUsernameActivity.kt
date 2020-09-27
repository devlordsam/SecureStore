package com.lordsam.securestore

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.AccountData

class ResetUsernameActivity : AppCompatActivity() {

    private lateinit var edtOldUsername: EditText
    private lateinit var edtNewUsername1: EditText
    private lateinit var edtNewUsername2: EditText
    private lateinit var btnCheck: Button
    private lateinit var btnSave: Button
    private var legit = false
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_username)
        sharedPref = getSharedPreferences("CreateAccount", MODE_PRIVATE)

        edtOldUsername = findViewById(R.id.editTextRUOldUsername)
        edtNewUsername1 = findViewById(R.id.editTextRUNewUsername1)
        edtNewUsername2 = findViewById(R.id.editTextRUNewUsername2)
        btnCheck = findViewById(R.id.buttonRUCheck)
        btnSave = findViewById(R.id.buttonRUSave)

        btnCheck.setOnClickListener {

            if (edtOldUsername.text.isNotEmpty()) {
                check(edtOldUsername.text.toString().trim())
            }else{
                Toast.makeText(this, "Enter old password!", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            if (edtNewUsername1.text.isEmpty()) {
                Toast.makeText(this, "Enter new password!", Toast.LENGTH_SHORT).show()
            }else if (edtNewUsername2.text.isEmpty()){
                Toast.makeText(this, "Re-enter new password!", Toast.LENGTH_SHORT).show()
            }else if (edtNewUsername2.text.toString() != edtNewUsername1.text.toString()){
                Toast.makeText(this, "Username not same!", Toast.LENGTH_SHORT).show()
            } else{
                savePass(edtNewUsername1.text.toString())
            }
        }
    }

    private fun check(oldUser: String) {
        val gson = Gson()
        val json = sharedPref.getString("accountData", "empty")
        val type  = object: TypeToken<AccountData>(){}.type
        val data = gson.fromJson<AccountData>(json, type)
        val getUser = data.email

        if (oldUser == getUser){
            Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show()
            legit = true
        }else{
            Toast.makeText(this, "Verification failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePass(newUser: String){
        val gson = Gson()
        val json = sharedPref.getString("accountData", "empty")
        val type  = object: TypeToken<AccountData>(){}.type
        val data = gson.fromJson<AccountData>(json, type)
        data.email = newUser
        val editor = sharedPref.edit()
        val jsonPut = gson.toJson(data)
        editor.putString("accountData", jsonPut)
        editor.apply().also {
            Toast.makeText(this, "Username Changed!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}