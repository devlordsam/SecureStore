package com.lordsam.securestore

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.AdharData
import com.lordsam.securestore.dataclass.CreditDebitData
import java.lang.Exception

class PopUpAdharActivity : AppCompatActivity() {


    private lateinit var bundle: Bundle
    private lateinit var edtName: EditText
    private lateinit var edtFatherName: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtMobile: EditText
    private lateinit var edtAdharNumber: EditText
    private lateinit var btnSave: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var arrayOfAdhar: ArrayList<AdharData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_adhar)
        sharedPref = getSharedPreferences("AdharDetails", MODE_PRIVATE)
        arrayOfAdhar = ArrayList()

        edtName = findViewById(R.id.editTextPUAAName)
        edtFatherName = findViewById(R.id.editTextPUAAFatherName)
        edtAddress = findViewById(R.id.editTextPUAAAddress)
        edtMobile = findViewById(R.id.editTextPUAAMobileNumber)
        edtAdharNumber = findViewById(R.id.editTextPUAAAdharNumber)
        btnSave = findViewById(R.id.buttonPUAASave)

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
            edtName.text.toString().isEmpty() -> {
                toast("Username")
                edtName.requestFocus()
            }
            edtFatherName.text.toString().isEmpty() -> {
                toast("Father's Name")
                edtFatherName.requestFocus()
            }
            edtAddress.text.toString().isEmpty() -> {
                toast("Address")
                edtAddress.requestFocus()
            }
            edtMobile.text.toString().isEmpty() -> {
                toast("Mobile Number")
                edtMobile.requestFocus()
            }
            edtAdharNumber.text.toString().isEmpty() -> {
                toast("Adhar Number")
                edtAdharNumber.requestFocus()
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
        val name = edtName.text.toString()
        val fatherName = edtFatherName.text.toString()
        val address = edtAddress.text.toString()
        val mobileNumber = edtMobile.text.toString().toLong()
        val adharNumber = edtAdharNumber.text.toString().toLong()
        val data = AdharData(name, fatherName, address, mobileNumber, adharNumber)

        val gson = Gson()

        try {
            val jsonGet = sharedPref.getString("adharData", "empty")

            if (jsonGet == "empty"){

                val editor = sharedPref.edit()
                arrayOfAdhar.add(data)
                val jsonPut = gson.toJson(arrayOfAdhar)
                editor.putString("adharData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("adharData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else {
                val type  = object: TypeToken<ArrayList<AdharData>>(){}.type
                val jsonData = gson.fromJson<ArrayList<AdharData>>(jsonGet, type)
                jsonData.add(data)

                val editor = sharedPref.edit()
                val jsonPut = gson.toJson(jsonData)
                editor.putString("adharData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("adharData", "empty") != "empty") {
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
        edtName.setText(bundle.getString("user"))
        edtFatherName.setText(bundle.getString("father"))
        edtAddress.setText(bundle.getString("address"))
        edtMobile.setText(bundle.getLong("mobile").toString())
        edtAdharNumber.setText(bundle.getLong("adharNumber").toString())
    }
}