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


    // define class fields
   private  var  connectButton: Button? = null
   private var urlList = arrayListOf<String>(

       "URL1",
       "URL2",
       "URL3",
       "URL4",
       "URL5")

    /**
     * The main part of the application that will come first
     */
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create our db and initialize it
       var db = Room.databaseBuilder(applicationContext,AppDB :: class.java, "URL_DB").
       fallbackToDestructiveMigration().build()

        val job = GlobalScope.launch {
            initDbAndList(db, urlList)
        }
         Thread.sleep(1000)


        // get the title and rotate it with animation
         var title = findViewById<TextView>(R.id.title)
         title.animate().rotation(360f).setDuration(2000)

        // animation of image of airplane
         var imageAp = findViewById<ImageView>(R.id.airplane_image)
         imageAp.translationX = -1000f
         imageAp.translationY = -1000f
         imageAp.animate().translationXBy(1000f).translationYBy(1000f)
             .rotationBy(3600f).setDuration(3500)



        connectButton = findViewById<Button>(R.id.connect)

        // create list of addresses for the url's of the server and show it
        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, urlList)
        var urlListView: ListView = findViewById<ListView>(R.id.listView)
        urlListView.adapter = arrayAdapter
        // show the clicked url in url text field
        urlListView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->

            val urlSelect = findViewById<EditText>(R.id.type_url)
            urlSelect.setText(urlList[i])
        }

        // handle the event of click on the connect button
        connectButton?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var lastUrl = findViewById<EditText>(R.id.type_url)
                var existInList = false
                var index = 0

                val urlSelect = findViewById<EditText>(R.id.type_url)

                // check if the clicked url is exist or not
                for (u in urlList) {
                    if (u.equals(lastUrl.text.toString())) {
                        existInList = true
                        index = urlList.indexOf(lastUrl.text.toString())
                        break
                    }
                }

                // update the order of the list
                if (existInList) {
                    while (index != 0) {
                        urlList[index] = urlList[index - 1]
                        index--
                    }
                    urlList[0] = lastUrl.text.toString()
                    urlListView.adapter = arrayAdapter
                }

                // remove the last url and update the list
                else {
                    urlList[4] = urlList[3]
                    urlList[3] = urlList[2]
                    urlList[2] = urlList[1]
                    urlList[1] = urlList[0]
                    urlList[0] = lastUrl.text.toString()
                    urlListView.adapter = arrayAdapter
                }

                // update the db in background
                GlobalScope.launch {
                    updateDB(db, urlList)
                }


                // check the connection with the selected server
                var c =  Command(0.0, 0.0, 0.0, 0.0)
                var conn = callServer(applicationContext)

                    var check = 0.0
                    check = conn.sendNetworkRequest(c, urlSelect.text.toString())
                    // if the selected server connected
                    if (check != -1.0) {
                        Toast.makeText(
                            applicationContext,
                            "Connect " + urlSelect.text,
                            Toast.LENGTH_SHORT
                        ).show()

                        // go to second activity of the app
                        moveToSecondActivity()
                    }
                    // show message of disconnect
                    else {
                        Toast.makeText(
                            applicationContext,
                            "unable to connect to server",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }

        })
    }

    /**
     * go to the next activity of the app and save the selected url of the server
     */
    private fun moveToSecondActivity() {

        val intent = Intent(this, JoyStick::class.java)
        val urlSelect = findViewById<EditText>(R.id.type_url)
        // "pass" the url to second activity
        intent.putExtra("url",urlSelect.text.toString())
        startActivity(intent)
    }

    /**
     * get the db and the updated url list and update the db
     */
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
    }

    /**
     * initialize the db and the url list
     */
    private fun initDbAndList (db : AppDB, urlList : ArrayList<String>) {

        /*
      db.urlDAO().readUrl().forEach{
          Log.i("Info","id is: ${it.URL_id}")
          Log.i("Info","url is: ${it.URL_name}")
      }*/

        // initialize 5 url
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