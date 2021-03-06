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
import com.lordsam.securestore.dataclass.PANData
import kotlinx.android.synthetic.main.card_cards.view.*
import kotlinx.android.synthetic.main.card_pan.view.*

class PanCardActivity : AppCompatActivity() {

    private lateinit var arrayOfPAN: ArrayList<PANData>
    private lateinit var sharedPref: SharedPreferences
    private lateinit var listViewPAN: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pan_card)
        sharedPref = getSharedPreferences("PANDetails", MODE_PRIVATE)
        arrayOfPAN = ArrayList()
        listViewPAN = findViewById(R.id.listViewPAN)

        loadData()
    }

    inner class ListViewCreditDebitAdapter(private val ctx : Context, private val arrOfPAN: ArrayList<PANData>): BaseAdapter(){
        override fun getCount(): Int {
            return arrOfPAN.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfPAN[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfPAN[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_pan, null)
            view.textViewCPANName.text = card.holder
            view.textViewCPANFather.text = card.fatherName
            view.textViewCPANNumber.text = card.number
            view.textViewCPANType.text = card.type
            view.setOnLongClickListener(View.OnLongClickListener {

                dialog(
                    ctx,
                    card.holder,
                    card.fatherName,
                    card.number,
                    card.type,
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
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpPANActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData(){

        val jsonGet = sharedPref.getString("PANData", "empty")
        val type  = object: TypeToken<ArrayList<PANData>>(){}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<PANData>>(jsonGet, type)
            arrayOfPAN = jsonData
            listViewPAN.adapter = ListViewCreditDebitAdapter(this, arrayOfPAN)
            listViewPAN.deferNotifyDataSetChanged()
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    private fun dialog(
        ctx: Context,
        holder: String,
        fatherName: String,
        number: String,
        type: String,
        index :Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Choose an action!")
            .setCancelable(false)
            .setPositiveButton(
                "Edit"
            ) { dialog, id ->

                delete(ctx, index)

                val intent = Intent(ctx, PopUpPANActivity::class.java)
                intent.putExtra("holder", holder)
                intent.putExtra("fatherName", fatherName)
                intent.putExtra("number", number)
                intent.putExtra("type", type)
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
        val jsonGet = sharedPref.getString("PANData", "empty")
        val type = object : TypeToken<ArrayList<PANData>>() {}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<PANData>>(jsonGet, type)
            jsonData.removeAt(index)
            val editor = sharedPref.edit()
            val jsonPut = gson.toJson(jsonData)
            editor.putString("PANData", jsonPut)
            editor.apply()

            listViewPAN.adapter = ListViewCreditDebitAdapter(ctx, jsonData)
            listViewPAN.deferNotifyDataSetChanged()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}