package com.lordsam.securestore

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.PANData
import com.lordsam.securestore.dataclass.WebsiteData
import java.lang.Exception

class PopUpWebsiteActivity : AppCompatActivity() {

    private lateinit var edtURL: EditText
    private lateinit var edtUserID: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnSave: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var arrayOfWeb: ArrayList<WebsiteData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_website)
        sharedPref = getSharedPreferences("WebsiteDetails", MODE_PRIVATE)
        arrayOfWeb = ArrayList()

        edtURL = findViewById(R.id.editTextPUWUrl)
        edtUserID = findViewById(R.id.editTextPUWloginId)
        edtPass = findViewById(R.id.editTextPUWPassword)
        btnSave = findViewById(R.id.buttonPUWSave)

        btnSave.setOnClickListener {
            validate()
        }
    }

    private fun validate(){

        when {
            edtURL.text.toString().isEmpty() -> {
                toast("URL")
                edtURL.requestFocus()
            }
            edtUserID.text.toString().isEmpty() -> {
                toast("User ID")
                edtUserID.requestFocus()
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
        val url = edtURL.text.toString()
        val userID = edtUserID.text.toString()
        val pass = edtPass.text.toString()
        val data = WebsiteData(url, userID, pass)

        val gson = Gson()

        try {
            val jsonGet = sharedPref.getString("WebsiteData", "empty")

            if (jsonGet == "empty"){

                val editor = sharedPref.edit()
                arrayOfWeb.add(data)
                val jsonPut = gson.toJson(arrayOfWeb)
                editor.putString("WebsiteData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("WebsiteData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else {
                val type  = object: TypeToken<ArrayList<WebsiteData>>(){}.type
                val jsonData = gson.fromJson<ArrayList<WebsiteData>>(jsonGet, type)
                jsonData.add(data)

                val editor = sharedPref.edit()
                val jsonPut = gson.toJson(jsonData)
                editor.putString("WebsiteData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("WebsiteData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }catch (ex : Exception){
            ex.printStackTrace()
        }
    }
}