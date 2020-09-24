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
import com.lordsam.securestore.dataclass.PANData
import com.lordsam.securestore.dataclass.WebsiteData
import kotlinx.android.synthetic.main.card_pan.view.*
import kotlinx.android.synthetic.main.card_website.view.*

class WebsiteActivity : AppCompatActivity() {

    private lateinit var arrayOfWeb: ArrayList<WebsiteData>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var listViewWeb: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website)
        sharedPref = getSharedPreferences("WebsiteDetails", MODE_PRIVATE)
        arrayOfWeb = ArrayList()
        listViewWeb = findViewById(R.id.listViewWeb)

        loadData()
    }

    class ListViewWebsiteAdapter(private val ctx : Context, private val arrOfWeb: ArrayList<WebsiteData>): BaseAdapter(){
        override fun getCount(): Int {
            return arrOfWeb.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfWeb[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfWeb[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_website, null)
            view.textViewCWURL.text = card.url
            view.textViewCWLoginId.text = card.userID
            view.textViewCWPassword.text = card.password

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
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpWebsiteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        val jsonGet = sharedPref.getString("WebsiteData", "empty")
        val type  = object: TypeToken<ArrayList<WebsiteData>>(){}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<WebsiteData>>(jsonGet, type)
            arrayOfWeb = jsonData
            listViewWeb.adapter = ListViewWebsiteAdapter(this, arrayOfWeb)
            listViewWeb.deferNotifyDataSetChanged()
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
}