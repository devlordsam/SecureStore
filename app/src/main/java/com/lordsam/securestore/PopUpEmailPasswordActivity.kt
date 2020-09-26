package com.lordsam.securestore

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.AdharData
import com.lordsam.securestore.dataclass.EmailPasswordData
import java.lang.Exception

class PopUpEmailPasswordActivity : AppCompatActivity() {

    private lateinit var bundle: Bundle
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnSave: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var arrayOfEP: ArrayList<EmailPasswordData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_email_password)
        sharedPref = getSharedPreferences("EmailPasswordDetails", MODE_PRIVATE)
        arrayOfEP = ArrayList()

        edtEmail = findViewById(R.id.editTextPUEPEmail)
        edtPass = findViewById(R.id.editTextPUEPPassword)
        btnSave = findViewById(R.id.buttonPUEPSave)

        try {
            bundle = intent.extras!!

            if (!bundle.isEmpty){
                setData()
            }
        }catch (ex :Exception){
            ex.printStackTrace()
        }

        btnSave.setOnClickListener {
            validate()
        }
    }

    private fun validate(){

        when {
            edtEmail.text.toString().isEmpty() -> {
                toast("Email")
                edtEmail.requestFocus()
            }
            edtPass.text.toString().isEmpty() -> {
                toast("Password")
                edtPass.requestFocus()
            }
            else -> {
                saveData()
            }
        }
    }

    private fun toast(msg: String){
        Toast.makeText(this, "Please fill $msg!", Toast.LENGTH_SHORT).show()
    }

    private fun saveData() {
        val email = edtEmail.text.toString()
        val pass = edtPass.text.toString()
        val data = EmailPasswordData(email, pass)

        val gson = Gson()

        try {
            val jsonGet = sharedPref.getString("emailPasswordData", "empty")

            if (jsonGet == "empty"){

                val editor = sharedPref.edit()
                arrayOfEP.add(data)
                val jsonPut = gson.toJson(arrayOfEP)
                editor.putString("emailPasswordData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("emailPasswordData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else {
                val type  = object: TypeToken<ArrayList<EmailPasswordData>>(){}.type
                val jsonData = gson.fromJson<ArrayList<EmailPasswordData>>(jsonGet, type)
                jsonData.add(data)

                val editor = sharedPref.edit()
                val jsonPut = gson.toJson(jsonData)
                editor.putString("emailPasswordData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("emailPasswordData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }catch (ex : Exception){
            ex.printStackTrace()
        }
    }

    private fun setData(){
        edtEmail.setText(bundle.getString("email"))
        edtPass.setText(bundle.getString("pass"))
    }
}