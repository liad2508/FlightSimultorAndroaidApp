package com.example.flightmobileapp


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.join
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.NonCancellable.join
import java.lang.Exception
import java.lang.String.join
import java.time.temporal.ValueRange
import kotlinx.coroutines.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


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

       var db = Room.databaseBuilder(applicationContext,AppDB :: class.java, "URL_DB").fallbackToDestructiveMigration().build()


/*
        Thread {
            initDbAndList(db, urlList)

        }.start()
        Thread.sleep(1000)*/


        val job = GlobalScope.launch {
            initDbAndList(db, urlList)
        }
        //job.join()








        connectButton = findViewById<Button>(R.id.connect)

        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, urlList)
        var urlListView: ListView = findViewById<ListView>(R.id.listView)
        urlListView.adapter = arrayAdapter
        urlListView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->

            val urlSelect = findViewById<EditText>(R.id.type_url)
            urlSelect.setText(urlList[i])
        }


        connectButton?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var lastUrl = findViewById<EditText>(R.id.type_url)
                var existInList = false
                var index = 0

                val urlSelect = findViewById<EditText>(R.id.type_url)


                Toast.makeText(
                    applicationContext,
                    "Connect " + urlSelect.text,
                    Toast.LENGTH_LONG
                ).show()

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

                Thread {
                    updateDB(db, urlList)
                }.start()
                moveToSecondActivity()
            }

        })
    }

    private fun moveToSecondActivity() {
       // val intent = Intent(this, SecondActivity::class.java)

         val intent = Intent(this, JoyStick::class.java)
        startActivity(intent)
    }

    private fun updateDB(db : AppDB, urlList : ArrayList<String>) {

        var index = 0
        for (u in urlList) {
            var urlEntity= URL_Entity()
            urlEntity.URL_id = index
            urlEntity.URL_name = u
            try {
                db.urlDAO().saveUrl(urlEntity)
            } catch (e : Exception) {
                db.urlDAO().updateUrl(urlEntity)
            }
            index++
        }

        /*
        db.urlDAO().readUrl().forEach{
            Log.i("Info","id is: ${it.URL_id}")
            Log.i("Info","url is: ${it.URL_name}")
        }*/
    }

    private suspend fun initDbAndList (db : AppDB, urlList : ArrayList<String>) {


      db.urlDAO().readUrl().forEach{
          Log.i("Info","id is: ${it.URL_id}")
          Log.i("Info","url is: ${it.URL_name}")
      }

        for (i in 0..4) {
            var urlEntity= URL_Entity()

            urlEntity.URL_id = 0
            var num = i + 1
            urlEntity.URL_name = "Localhost $num"
            try {
                db.urlDAO().saveUrl(urlEntity)
            } catch (e : Exception) {
                urlEntity.URL_name = db.urlDAO().readUrl().get(i).URL_name
            }
               urlList[i] = urlEntity.URL_name

        }
    }


}