package com.lordsam.securestore

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.PassportData
import com.lordsam.securestore.dataclass.WebsiteData
import kotlinx.android.synthetic.main.card_passport.view.*
import kotlinx.android.synthetic.main.card_website.view.*

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

    class ListViewPassportAdapter(private val ctx : Context, private val arrOfPP: ArrayList<PassportData>): BaseAdapter(){
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
            view.textviewCPassportName.text = card.username
            view.textviewCPassportNationality.text = card.nationality
            view.textviewCPassportAddress.text = card.address
            view.textviewCPassportNumber.text = card.passportNumber

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
}