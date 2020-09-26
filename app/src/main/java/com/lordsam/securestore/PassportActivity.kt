package com.lordsam.securestore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.CreditDebitData
import com.lordsam.securestore.dataclass.PassportData
import kotlinx.android.synthetic.main.card_passport.view.*

class PassportActivity : AppCompatActivity() {

    private lateinit var arrayOfPP: ArrayList<PassportData>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var listViewPP: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passport)
        sharedPref = getSharedPreferences("PassportDetails", MODE_PRIVATE)
        arrayOfPP = ArrayList()
        listViewPP = findViewById(R.id.listViewPassport)

        loadData()
    }

   inner class ListViewPassportAdapter(private val ctx : Context, private val arrOfPP: ArrayList<PassportData>): BaseAdapter(){
        override fun getCount(): Int {
            return arrOfPP.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfPP[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfPP[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_passport, null)
            view.textViewCPassportName.text = card.username
            view.textViewCPassportNationality.text = card.nationality
            view.textViewCPassportAddress.text = card.address
            view.textViewCPassportNumber.text = card.passportNumber
            view.setOnLongClickListener(View.OnLongClickListener {
            dialog(
                ctx,
                card.username,
                card.nationality,
                card.address,
                card.passportNumber,
                p0
            )
            return@OnLongClickListener false
        })

            return view
        }

    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpPassportActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        val jsonGet = sharedPref.getString("PassportData", "empty")
        val type  = object: TypeToken<ArrayList<PassportData>>(){}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<PassportData>>(jsonGet, type)
            arrayOfPP = jsonData
            listViewPP.adapter = ListViewPassportAdapter(this, arrayOfPP)
            listViewPP.deferNotifyDataSetChanged()
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    private fun dialog(
        ctx: Context,
        username: String,
        nationality: String,
        address: String,
        passportNumber: String,
        index :Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Choose an action!")
            .setCancelable(false)
            .setPositiveButton(
                "Edit"
            ) { dialog, id ->

                delete(ctx, index)

                val intent = Intent(ctx, PopUpPassportActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("nationality", nationality)
                intent.putExtra("address", address)
                intent.putExtra("passportNumber", passportNumber)
                startActivity(intent)
            }
            .setNegativeButton(
                "Delete"
            ) { dialog, id ->
                delete(ctx, index)
                dialog.cancel()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun delete(
        ctx: Context,
        index :Int
    ) {
        val jsonGet = sharedPref.getString("PassportData", "empty")
        val type = object : TypeToken<ArrayList<PassportData>>() {}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<PassportData>>(jsonGet, type)
            jsonData.removeAt(index)
            val editor = sharedPref.edit()
            val jsonPut = gson.toJson(jsonData)
            editor.putString("PassportData", jsonPut)
            editor.apply()

            listViewPP.adapter = ListViewPassportAdapter(ctx, jsonData)
            listViewPP.deferNotifyDataSetChanged()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}