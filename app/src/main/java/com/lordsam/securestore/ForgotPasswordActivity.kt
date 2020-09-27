package com.lordsam.securestore

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.AccountData

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var edtAnswer: EditText
    private lateinit var edtNewPass1: EditText
    private lateinit var edtNewPass2: EditText
    private lateinit var btnCheck: Button
    private lateinit var btnSave: Button
    private var legit = false
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        sharedPref = getSharedPreferences("CreateAccount", MODE_PRIVATE)

        spinner = findViewById(R.id.spinnerFPQuestion)
        edtAnswer = findViewById(R.id.editTextFPAnswer)
        edtNewPass1 = findViewById(R.id.editTextFPNewPass1)
        edtNewPass2 = findViewById(R.id.editTextFPNewPass2)
        btnCheck = findViewById(R.id.buttonFPCheck)
        btnSave = findViewById(R.id.buttonFPSave)

        btnCheck.setOnClickListener {

            if (edtAnswer.text.isNotEmpty()) {
                check(spinner.selectedItem.toString(), edtAnswer.text.toString().trim())
            }else{
                Toast.makeText(this, "Enter Answer!", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            if (edtNewPass1.text.isEmpty()) {
                Toast.makeText(this, "Enter new password!", Toast.LENGTH_SHORT).show()
            }else if (edtNewPass2.text.isEmpty()){
                Toast.makeText(this, "Re-enter new password!", Toast.LENGTH_SHORT).show()
            }else if (edtNewPass2.text.toString() != edtNewPass1.text.toString()){
                Toast.makeText(this, "Password not same!", Toast.LENGTH_SHORT).show()
            } else{
                savePass(edtNewPass1.text.toString())
            }
        }
    }

    private fun check(question: String, answer: String) {
        val gson = Gson()
        val json = sharedPref.getString("accountData", "empty")
        val type  = object: TypeToken<AccountData>(){}.type
        val data = gson.fromJson<AccountData>(json, type)
        val getQuestion = data.query
        val getAnswer = data.answer

        if (getQuestion == question && getAnswer == answer){
            Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show()
            legit = true
        }else{
            Toast.makeText(this, "Verification failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePass(newPass: String) {

        if (legit) {
            val gson = Gson()
            val json = sharedPref.getString("accountData", "empty")
            val type = object : TypeToken<AccountData>() {}.type
            val data = gson.fromJson<AccountData>(json, type)
            data.pass = newPass
            val editor = sharedPref.edit()
            val jsonPut = gson.toJson(data)
            editor.putString("accountData", jsonPut)
            editor.apply().also {
                Toast.makeText(this, "Password Changed!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
            Toast.makeText(this, "Please verify first!", Toast.LENGTH_SHORT).show()
        }
    }
}