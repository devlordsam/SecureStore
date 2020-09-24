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
import com.lordsam.securestore.dataclass.CreditDebitData
import java.lang.Exception

class PopUpCreditDebitActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var edtHolder: EditText
    private lateinit var edtAccount: EditText
    private lateinit var edtExMonth: EditText
    private lateinit var edtExYear: EditText
    private lateinit var edtCvv: EditText
    private lateinit var cardTypeSpinner: Spinner
    private lateinit var btnSave: Button
    private lateinit var arrayOfCards: ArrayList<CreditDebitData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_credit_debit)
        sharedPref = getSharedPreferences("creditDebitData", MODE_PRIVATE)
        arrayOfCards = ArrayList()

        edtHolder = findViewById(R.id.editTextPUCDHolderName)
        edtAccount = findViewById(R.id.editTextPUCDAccountNumber)
        edtExMonth = findViewById(R.id.editTextPUCDExpiryMonth)
        edtExYear = findViewById(R.id.editTextPUCDExpiryYear)
        edtCvv = findViewById(R.id.editTextPUCDCvv)
        cardTypeSpinner = findViewById(R.id.spinnerPUCDCardType)
        btnSave = findViewById(R.id.buttonPUCDSave)

        btnSave.setOnClickListener {
            validate()
        }
    }

    private fun validate(){

        when {
            edtHolder.text.toString().isEmpty() -> {
                toast("Holder Name")
                edtHolder.requestFocus()
            }
            edtAccount.text.toString().isEmpty() -> {
                toast("Account Number")
                edtAccount.requestFocus()
            }
            edtExMonth.text.toString().isEmpty() -> {
                toast("Expiry Month")
                edtExMonth.requestFocus()
            }
            edtExYear.text.toString().isEmpty() -> {
                toast("Expiry Year")
                edtExYear.requestFocus()
            }
            edtCvv.text.toString().isEmpty() -> {
                toast("CVV Number")
                edtCvv.requestFocus()
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

        val name = edtHolder.text.toString()
        val account = edtAccount.text.toString().toLong()
        val month = edtExMonth.text.toString().toInt()
        val year = edtExYear.text.toString().toInt()
        val cvv = edtCvv.text.toString().toInt()
        val cardType = cardTypeSpinner.selectedItem.toString()
        val data = CreditDebitData(name, account, month, year, cvv, cardType)

        val gson = Gson()

        try {
            val jsonGet = sharedPref.getString("creditDebitCardData", "empty")

            if (jsonGet == "empty"){

                val editor = sharedPref.edit()
                arrayOfCards.add(data)
                val jsonPut = gson.toJson(arrayOfCards)
                editor.putString("creditDebitCardData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("creditDebitCardData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }else {
                val type  = object: TypeToken<ArrayList<CreditDebitData>>(){}.type
                val jsonData = gson.fromJson<ArrayList<CreditDebitData>>(jsonGet, type)
                jsonData.add(data)

                val editor = sharedPref.edit()
                val jsonPut = gson.toJson(jsonData)
                editor.putString("creditDebitCardData", jsonPut)
                editor.apply().also {
                    if (sharedPref.getString("creditDebitCardData", "empty") != "empty") {
                        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }catch (ex :Exception){
            ex.printStackTrace()
        }
    }
}