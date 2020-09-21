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
import com.lordsam.securestore.dataclass.AdharData
import com.lordsam.securestore.dataclass.CreditDebitData
import kotlinx.android.synthetic.main.card_adhar.view.*
import kotlinx.android.synthetic.main.card_cards.view.*

class AdharActivity : AppCompatActivity() {

    private lateinit var arrayOfAdhar: ArrayList<AdharData>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var listViewAdhar: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adhar)
        sharedPref = getSharedPreferences("AdharDetails", MODE_PRIVATE)
        arrayOfAdhar = ArrayList()
        listViewAdhar = findViewById(R.id.listViewAdhar)

        loadData()
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    class ListViewAdharAdapter(val ctx : Context, val arrOfAdhar: ArrayList<AdharData>): BaseAdapter(){
        override fun getCount(): Int {
            return arrOfAdhar.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfAdhar[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfAdhar[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_adhar, null)
            view.textViewCAName.text = card.userName
            view.textViewCAFatherName.text = card.fatherName
            view.textViewCAAddress.text = card.address
            view.textViewCAMobile.text = card.mobile.toString()
            view.textViewCAAdharNumber.text = card.adharNumber.toString()
            return view
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpAdharActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        val jsonGet = sharedPref.getString("adharData", "empty")
        val type  = object: TypeToken<ArrayList<AdharData>>(){}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<AdharData>>(jsonGet, type)
            arrayOfAdhar.clear()
            arrayOfAdhar = jsonData
            listViewAdhar.adapter = ListViewAdharAdapter(this, arrayOfAdhar)
            listViewAdhar.deferNotifyDataSetChanged()
        }catch (ex :Exception){
            ex.printStackTrace()
        }
    }
}