package com.lordsam.securestore

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.PassportData
import com.lordsam.securestore.dataclass.WebsiteData
import java.lang.Exception

class PopUpPassportActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtNationality: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtPassportNumber: EditText
    private lateinit var btnSave: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var arrayOfPP: ArrayList<PassportData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_passport)
        sharedPref = getSharedPreferences("PassportDetails", MODE_PRIVATE)
        arrayOfPP = ArrayList()

        edtName = findViewById(R.id.editTextPUPassportName)
        edtNationality = findViewById(R.id.editTextPUPassportNationality)
        edtAddress = findViewById(R.id.editTextPUPassportAddress)
        edtPassportNumber = findViewById(R.id.editTextPUPassportNumber)
        btnSave = findViewById(R.id.buttonPUPassportSave)

        btnSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val name = edtName.text.toString()
        val nationality = edtNationality.text.toString()
        val address = edtAddress.text.toString()
        val number = edtPassportNumber.text.toString()
        val data = PassportData(name, nationality, address, number)

        val gson = Gson()

        try {
            val jsonGet = sharedPref.getString("PassportData", "empty")

            if (jsonGet == "empty"){

                val editor = sharedPref.edit()
                arrayOfPP.add(data)
                val jsonPut = gson.toJson(arrayOfPP)
                editor.putString("PassportData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("PassportData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else {
                val type  = object: TypeToken<ArrayList<PassportData>>(){}.type
                val jsonData = gson.fromJson<ArrayList<PassportData>>(jsonGet, type)
                jsonData.add(data)

                val editor = sharedPref.edit()
                val jsonPut = gson.toJson(jsonData)
                editor.putString("PassportData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("PassportData", "empty") != "empty") {
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