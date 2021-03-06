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
import com.lordsam.securestore.dataclass.PANData
import java.lang.Exception

class PopUpPANActivity : AppCompatActivity() {

    private lateinit var bundle: Bundle
    private lateinit var edtName: EditText
    private lateinit var edtFatherName: EditText
    private lateinit var edtPANNumber: EditText
    private lateinit var edtPANType: EditText
    private lateinit var btnSave: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var arrayOfPAN: ArrayList<PANData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_p_a_n)
        sharedPref = getSharedPreferences("PANDetails", MODE_PRIVATE)
        arrayOfPAN = ArrayList()

        edtName = findViewById(R.id.editTextPUPANName)
        edtFatherName = findViewById(R.id.editTextPUPANFather)
        edtPANNumber = findViewById(R.id.editTextPUPANNumber)
        edtPANType = findViewById(R.id.editTextPUPANType)
        btnSave = findViewById(R.id.buttonPUPANSave)

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
                toast("Holder Name")
                edtName.requestFocus()
            }
            edtFatherName.text.toString().isEmpty() -> {
                toast("Father's Name")
                edtFatherName.requestFocus()
            }
            edtPANNumber.text.toString().isEmpty() -> {
                toast("PAN Number")
                edtPANNumber.requestFocus()
            }
            edtPANType.text.toString().isEmpty() -> {
                toast("PAN Type")
                edtPANType.requestFocus()
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
        val number = edtPANNumber.text.toString()
        val typePAN = edtPANType.text.toString()
        val data = PANData(name, fatherName, number, typePAN)

        val gson = Gson()

        try {
            val jsonGet = sharedPref.getString("PANData", "empty")

            if (jsonGet == "empty"){

                val editor = sharedPref.edit()
                arrayOfPAN.add(data)
                val jsonPut = gson.toJson(arrayOfPAN)
                editor.putString("PANData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("PANData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else {
                val type  = object: TypeToken<ArrayList<PANData>>(){}.type
                val jsonData = gson.fromJson<ArrayList<PANData>>(jsonGet, type)
                jsonData.add(data)

                val editor = sharedPref.edit()
                val jsonPut = gson.toJson(jsonData)
                editor.putString("PANData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("PANData", "empty") != "empty") {
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
        edtName.setText(bundle.getString("holder"))
        edtFatherName.setText(bundle.getString("fatherName"))
        edtPANNumber.setText(bundle.getString("number"))
        edtPANType.setText(bundle.getString("type"))
    }
}