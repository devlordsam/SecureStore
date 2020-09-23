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
import com.lordsam.securestore.dataclass.CreditDebitData
import com.lordsam.securestore.dataclass.EmailPasswordData
import kotlinx.android.synthetic.main.card_cards.view.*
import kotlinx.android.synthetic.main.card_email_password.view.*

class EmailPasswordActivity : AppCompatActivity() {

    private lateinit var arrayOfEP: ArrayList<EmailPasswordData>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var listViewEP: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)

        sharedPref = getSharedPreferences("EmailPasswordDetails", MODE_PRIVATE)
        arrayOfEP = ArrayList()
        listViewEP = findViewById(R.id.listViewEP)

        loadData()
    }

    class ListViewEmailPasswordAdapter(private val ctx : Context, private val arrOfEP: ArrayList<EmailPasswordData>): BaseAdapter(){
        override fun getCount(): Int {
            return arrOfEP.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfEP[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfEP[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_email_password, null)
            view.textViewCEPEmail.text = card.email
            view.textViewCEPPass.text = card.pass
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
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpEmailPasswordActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        val jsonGet = sharedPref.getString("emailPasswordData", "empty")
        val type  = object: TypeToken<ArrayList<EmailPasswordData>>(){}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<EmailPasswordData>>(jsonGet, type)
            arrayOfEP.clear()
            arrayOfEP = jsonData
            listViewEP.adapter = ListViewEmailPasswordAdapter(this, arrayOfEP)
            listViewEP.deferNotifyDataSetChanged()
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }
}