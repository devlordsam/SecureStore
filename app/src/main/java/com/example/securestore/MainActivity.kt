package com.example.securestore


import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dataclass.DataListView
import kotlinx.android.synthetic.main.card_view.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.mainListView)

        val n1 = DataListView("name", "image")
        val n2 = DataListView("Name", "image")
        val name = arrayOf("Aman", "Raj")
        val image = arrayOf("Hi", "Hello")

        val data = arrayOf(n1, n2)
        listView.adapter = BaseAdapter(this, name, image)

    }
    inner class BaseAdapter(context: Context,name:String):android.widget.BaseAdapter(){

        val mContext = context
        val Name =   name

        override fun getCount(): Int {
            return  Name.length
        }

        override fun getItem(position: Int): Any {
            return Name[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val card = layoutInflater.inflate(R.layout.card_view,null,true)
            card.cardTVName.text = Name
        }

    }
}
