package com.example.securestore


import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.securestore.dataclass.DataListView
import kotlinx.android.synthetic.main.card_view.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var arrayOfTokens = ArrayList<DataListView>()
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listViewMain)

        arrayOfTokens.add(DataListView("Credit/Debit Card", R.drawable.credit_card))
        arrayOfTokens.add(DataListView("Aadhaar", R.drawable.adhar))
        arrayOfTokens.add(DataListView("E-mail/Password", R.drawable.email_pass))
        arrayOfTokens.add(DataListView("Pan Card", R.drawable.pan_card))
        arrayOfTokens.add(DataListView("Passport", R.drawable.passport))
        arrayOfTokens.add(DataListView("Website Login", R.drawable.website))

        listAdapter = ListAdapter(this, arrayOfTokens)
        listView.adapter = listAdapter

    }


    class ListAdapter(private val context: Context, private val arrayOfTokens: ArrayList<DataListView>): BaseAdapter(){


        override fun getCount(): Int {
            return  arrayOfTokens.size
        }

        override fun getItem(position: Int): Any {
            return arrayOfTokens[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val token = arrayOfTokens[position]
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.card_view,null)
            view.textViewCVTitle.text = token.title
            view.imageViewCV.setBackgroundResource(token.image)

            view.setOnClickListener {
                Toast.makeText(context, token.title, Toast.LENGTH_SHORT).show()
            }

            return view
        }

    }
}
