package com.lordsam.securestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lordsam.securestore.dataclass.AdharData
import kotlinx.android.synthetic.main.card_adhar.view.*


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

    inner class ListViewAdharAdapter(
        private val ctx: Context,
        private val arrOfAdhar: ArrayList<AdharData>
    ) : BaseAdapter() {
        override fun getCount(): Int {
            return arrOfAdhar.size
        }

        override fun getItem(p0: Int): Any {
            return arrOfAdhar[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val card = arrOfAdhar[p0]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.card_adhar, null)
            view.textViewCAName.text = card.userName
            view.textViewCAFatherName.text = card.fatherName
            view.textViewCAAddress.text = card.address
            view.textViewCAMobile.text = card.mobile.toString()
            view.textViewCAAdharNumber.text = card.adharNumber.toString()
            view.setOnLongClickListener(View.OnLongClickListener {

                dialog(
                    ctx,
                    card.userName,
                    card.fatherName,
                    card.address,
                    card.mobile,
                    card.adharNumber,
                    p0
                )
                return@OnLongClickListener false
            })
            return view
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuItemAdd -> startActivity(Intent(this, PopUpAdharActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {

        val jsonGet = sharedPref.getString("adharData", "empty")
        val type = object : TypeToken<ArrayList<AdharData>>() {}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<AdharData>>(jsonGet, type)
            arrayOfAdhar = jsonData
            listViewAdhar.adapter = ListViewAdharAdapter(this, arrayOfAdhar)
            listViewAdhar.deferNotifyDataSetChanged()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun dialog(
        ctx: Context,
        user: String,
        father: String,
        address: String,
        mobile: Long,
        adharNumber: Long,
        index :Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Choose an action!")
            .setCancelable(false)
            .setPositiveButton(
                "Edit"
            ) { dialog, id ->

                delete(ctx, index)

                val intent = Intent(ctx, PopUpAdharActivity::class.java)
                intent.putExtra("user", user)
                intent.putExtra("father", father)
                intent.putExtra("address", address)
                intent.putExtra("mobile", mobile)
                intent.putExtra("adharNumber", adharNumber)
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
        val jsonGet = sharedPref.getString("adharData", "empty")
        val type = object : TypeToken<ArrayList<AdharData>>() {}.type
        val gson = Gson()

        try {
            val jsonData = gson.fromJson<ArrayList<AdharData>>(jsonGet, type)
            jsonData.removeAt(index)
            val editor = sharedPref.edit()
            val jsonPut = gson.toJson(jsonData)
            editor.putString("adharData", jsonPut)
            editor.apply()

            listViewAdhar.adapter = ListViewAdharAdapter(ctx, jsonData)
            listViewAdhar.deferNotifyDataSetChanged()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}