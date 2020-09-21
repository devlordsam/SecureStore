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
import kotlinx.android.synthetic.main.card_cards.view.*

class CreditDebitCardActivity : AppCompatActivity() {

    private lateinit var arrayOfCards: ArrayList<CreditDebitData>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var listViewCD: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_debit_card)
        sharedPref = getSharedPreferences("creditDebitData", MODE_PRIVATE)
        arrayOfCards = ArrayList()
        listViewCD = findViewById(R.id.listViewCD)

        loadData()
    }

    class ListViewCreditDebitAdapter(val ctx :Context, val arrOfCards: ArrayList<CreditDebitData>): BaseAdapter(){
        override fun getCount(): Int {
            return arrOfCards.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfCards[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfCards[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_cards, null)
            view.textViewCCHolderName.text = card.holderName
            view.textViewCCAccountNumber.text = card.accountNumber.toString()
            view.textViewCCExpiryMonth.text = card.expiryMonth.toString()
            view.textViewCCExpiryYear.text = card.expiryYear.toString()
            view.textViewCCCVV.text = card.cvv.toString()
            view.textViewCCCardType.text = card.cardType
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
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpCreditDebitActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        val jsonGet = sharedPref.getString("creditDebitCardData", "empty")
        val type  = object: TypeToken<ArrayList<CreditDebitData>>(){}.type
        val gson = Gson()
        val jsonData = gson.fromJson<ArrayList<CreditDebitData>>(jsonGet, type)
        arrayOfCards.clear()
        arrayOfCards = jsonData
        listViewCD.adapter = ListViewCreditDebitAdapter(this, arrayOfCards)
        listViewCD.deferNotifyDataSetChanged()
    }
}