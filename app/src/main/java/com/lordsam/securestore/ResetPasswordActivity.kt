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

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var edtOldPass: EditText
    private lateinit var edtNewPass1: EditText
    private lateinit var edtNewPass2: EditText
    private lateinit var btnCheck: Button
    private lateinit var btnSave: Button
    private var legit = false
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        sharedPref = getSharedPreferences("CreateAccount", MODE_PRIVATE)

        edtOldPass = findViewById(R.id.editTextRPOldPass)
        edtNewPass1 = findViewById(R.id.editTextRPNewPass1)
        edtNewPass2 = findViewById(R.id.editTextRPNewPass2)
        btnCheck = findViewById(R.id.buttonRPCheck)
        btnSave = findViewById(R.id.buttonRPSave)

        btnCheck.setOnClickListener {

            if (edtOldPass.text.isNotEmpty()) {
                check(edtOldPass.text.toString().trim())
            }else{
                Toast.makeText(this, "Enter old password!", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            if (edtNewPass1.text.isEmpty()) {
                Toast.makeText(this, "Enter new password!", Toast.LENGTH_SHORT).show()
            }else if (edtNewPass2.text.isEmpty()){
                Toast.makeText(this, "Re-enter new password!", Toast.LENGTH_SHORT).show()
            }else if (edtNewPass1.text.toString() != edtNewPass2.text.toString()){
                Toast.makeText(this, "Password not same!", Toast.LENGTH_SHORT).show()
            } else{
                savePass(edtNewPass1.text.toString())
            }
        }
    }

    private fun check(oldPass: String) {
        val gson = Gson()
        val json = sharedPref.getString("accountData", "empty")
        val type  = object: TypeToken<AccountData>(){}.type
        val data = gson.fromJson<AccountData>(json, type)
        val getPass = data.pass

        if (oldPass == getPass){
            Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show()
            legit = true
        }else{
            Toast.makeText(this, "Verification failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePass(newPass: String){
        val gson = Gson()
        val json = sharedPref.getString("accountData", "empty")
        val type  = object: TypeToken<AccountData>(){}.type
        val data = gson.fromJson<AccountData>(json, type)
        data.pass = newPass
        val editor = sharedPref.edit()
        val jsonPut = gson.toJson(data)
        editor.putString("accountData", jsonPut)
        editor.apply().also {
            Toast.makeText(this, "Password Changed!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}