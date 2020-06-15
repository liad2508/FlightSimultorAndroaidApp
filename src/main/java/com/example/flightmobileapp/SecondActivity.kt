package com.example.flightmobileapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class SecondActivity : AppCompatActivity() {

    var throttleSeekBar : SeekBar? = null
    var rudderSeekBar : SeekBar? = null
    var linearLayout : LinearLayout? = null
    var currentThrottle: Double = 0.0
    var lastThrottle: Double = 0.0
    var currentRudder: Double = 0.0
    var lastRudder: Double = 0.0
    var relativeChangeThrottle : Double = 0.01
    var relativeChangeRudder: Double = 0.02
    var isChange : Boolean = false
    var checkObject = callServer()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.i("info", "create second activity");
        throttleSeekBar = findViewById<SeekBar>(R.id.throttle)
        rudderSeekBar = findViewById<SeekBar>(R.id.rudder)
        getScreenShot()


        /*GlobalScope.launch { // launch a new coroutine in background and continue
            delay(3000L) // non-blocking delay for 1 second (default time unit is ms)
            getScreenShot() // print after delay
        }
        println("Hello,") // main thread continues while coroutine is delayed
        Thread.sleep(3000L)*/

        throttleSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentThrottle = progress.toDouble() / 100.0;
                var changeInThrottle : Double = 0.0
                changeInThrottle = currentThrottle - lastThrottle
                if (changeInThrottle < 0) {
                    changeInThrottle *= -1
                }

                // check if changed in more than 1 %
                if (changeInThrottle > relativeChangeThrottle) {
                    // send request to server
                    sendDataToServer()
                }
                lastThrottle = currentThrottle
                Log.i("info", currentThrottle.toString())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        rudderSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentRudder = (progress.toDouble() - 100) / 100.0
                var changeInRudder : Double = 0.0
                changeInRudder = currentRudder - lastRudder
                if (changeInRudder < 0) {
                    changeInRudder *= -1
                }
                if (changeInRudder > relativeChangeRudder) {
                    // send to server
                    getScreenShot() // TODO - should not be here, just for check!
                    //sendDataToServer() // TODO - should be here!
                }
                Log.i("info", currentRudder.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        if (isChange) {
          // var command = Command(currentThrottle, currentRudder, 0.0, 0.1)
          //  checkObject.sendNetworkRequest(command)
            isChange = false
        }
    }

    private fun getScreenShot() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:54047/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) { // Request was succeed
                if(response.isSuccessful) {
                    Log.i("info", "Succeed in GET SCREEN SHOT!!!")
                    val I = response?.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread {
                        val imageView =
                            findViewById<View>(R.id.image_simulator) as ImageView
                        //imageView.setImageResource(R.drawable.ic_launcher_background);
                        imageView.setImageBitmap(B)
                        // image_simulator.setImageBitmap(B)
                    }
                }

                Log.i("info", "Outside isSuccessful response block in Screen Shot")
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Request was not succeed
                Log.i("info", "Request was not succeed IN SCREENSHOT!")
            }
        })

    }

    private fun sendDataToServer() {
        /* By Daniels
        Log.i("info", "change image")
        var image : ImageView = findViewById<ImageView>(R.id.image_simulator)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:60369/api/Command/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) { // Request was succeed
                Log.i("info", "Succeed!!!")
                val I = response?.body()?.byteStream()
                val B = BitmapFactory.decodeStream(I)
                runOnUiThread {
                    image.setImageBitmap(B)
                }

            } override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Request was not succeed
                Log.i("info", "Request was not succeed!")
            }
        })*/

       var command = Command(currentThrottle, currentRudder, 0.0, 0.1)
        checkObject.sendNetworkRequest(command)
    }


    private fun sendNetworkRequest(command: Command)  {

    }

    private fun updateImageFromSimulator(view: View) {
        /*Log.i("info", "change image")
        var image : ImageView = findViewById<ImageView>(R.id.image_simulator)
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:60369/api/Command/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        val body = api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) { // Request was succeed
                Log.i("info", "Succeed!!!")
                val I = response?.body()?.byteStream()
                val B = BitmapFactory.decodeStream(I)
                runOnUiThread {
                    image.setImageBitmap(B)
                }


            } override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Request was not succeed
                Log.i("info", "Request was not succeed!")
            }
        })*/
        //image.setImageResource(R.drawable.//name of image)
    }

}