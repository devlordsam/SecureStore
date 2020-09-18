package com.example.securestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = arrayListOf<String>("Aman","Raj","Sarvesh","akansha","Rukaiya")
        var image = arrayOf("1","2","3","4","5")
        val myAdapter = ArrayAdapter<String>(this,R.layout.card_view,name)

        val listView:ListView = findViewById<ListView>(R.id.mainListView)
        listView.adapter = myAdapter


    }
}