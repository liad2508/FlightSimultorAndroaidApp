package com.example.flightmobileapp


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity

import kotlin.collections.ArrayList


class MainActivity1 : AppCompatActivity() {

   private var connectButton: Button? = null
   private var urlList = arrayListOf<String>(
       "URL1",
       "URL2",
       "URL3",
       "URL4",
       "URL5")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectButton = findViewById<Button>(R.id.connect)

        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, urlList)
        var urlListView: ListView = findViewById<ListView>(R.id.listView)
        urlListView.adapter = arrayAdapter
        urlListView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(
                applicationContext,
                "Connect " + urlList[i],
                Toast.LENGTH_LONG
            ).show()
            val urlSelect = findViewById<EditText>(R.id.type_url)
            urlSelect.setText(urlList[i])
        }


        connectButton?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var lastUrl = findViewById<EditText>(R.id.type_url)
                var existInList = false
                var index = 0

                for (u in urlList) {

                    if (u.equals(lastUrl.text.toString())) {
                        existInList = true
                        index = urlList.indexOf(lastUrl.text.toString())
                        break
                    }
                }

                if (existInList) {
                    while (index != 0) {
                        urlList[index] = urlList[index - 1]
                        index--
                    }
                    urlList[0] = lastUrl.text.toString()
                    urlListView.adapter = arrayAdapter
                }
                else {

                    urlList[4] = urlList[3]
                    urlList[3] = urlList[2]
                    urlList[2] = urlList[1]
                    urlList[1] = urlList[0]
                    urlList[0] = lastUrl.text.toString()
                    urlListView.adapter = arrayAdapter
                }

                moveToSecondActivity()
            }

        })
    }

    private fun moveToSecondActivity() {



       // val intent = Intent(this, SecondActivity::class.java)
         val intent = Intent(this, JoyStick::class.java)
        startActivity(intent)

    }


}