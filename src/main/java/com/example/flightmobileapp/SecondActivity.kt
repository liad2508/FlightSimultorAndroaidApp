package com.example.flightmobileapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

open class SecondActivity : AppCompatActivity() {

    // define class fields
    var throttleSeekBar : SeekBar? = null
    var rudderSeekBar : SeekBar? = null
    var linearLayout : LinearLayout? = null
    var currentThrottle: Double = 0.0
    var lastThrottle: Double = 0.0
    var currentRudder: Double = 0.0
    var changeInThrottle : Double = 0.0
    var lastRudder: Double = 0.0
    var aileron : Double = 0.0
    var elevator : Double = 0.0
    var changeInRudder : Double = 0.0
    var relativeChangeThrottle : Double = 0.01
    var relativeChangeRudder: Double = 0.02
    var isChange : Boolean = false
    var checkObject : callServer? = null
    var toast : Toast? = null


    /**
     * The main part of the application that will come first
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        // create instance of callServer to communicate with the server
        checkObject = callServer(applicationContext)
        val chosenURL = intent.getStringExtra("url")
        // get the elements from the view
        throttleSeekBar = findViewById<SeekBar>(R.id.throttle)
        rudderSeekBar = findViewById<SeekBar>(R.id.rudder)


        // send request in coroutine scope to the server to get the image from the simulator
        GlobalScope.launch {
            while (true) {
                try {
                    getScreenShot(chosenURL)
                    delay(5000)
                } catch (e : Exception) {

                }

            }
        }

        // handle the event of change the throttle seek bar
        throttleSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // calculate the new value
                currentThrottle = progress.toDouble() / 100.0;
                // check if the value change in more than 1 % from the last value
                changeInThrottle = currentThrottle - lastThrottle
                if (changeInThrottle < 0) {
                    changeInThrottle *= -1
                }

                // check if changed in more than 1 %
                if (changeInThrottle > relativeChangeThrottle) {
                    // send request to server
                    GlobalScope.launch {

                        sendDataToServer(chosenURL)
                    }

                }
                // update the last value
                lastThrottle = currentThrottle

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        // handle the event of change the rudder seek bar
        rudderSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // calculate the new value
                currentRudder = (progress.toDouble() - 100) / 100.0

                // check if the value change in more than 1 % from the last value
                changeInRudder = currentRudder - lastRudder
                if (changeInRudder < 0) {
                    changeInRudder *= -1
                }
                if (changeInRudder > relativeChangeRudder) {
                    // send to server
                    GlobalScope.launch {
                        sendDataToServer(chosenURL)
                    }
                }

                lastRudder = currentRudder
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        if (isChange) {
            isChange = false
        }
    }

    /**
     * function that get url of the server and return screenShot from the simulator
     */
    private fun getScreenShot(url : String) {
        //http://10.0.2.2:54047/
        val gson = GsonBuilder()
            .setLenient()
            .create()
        // using retrofit to send the request
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)
        /*val body =*/ api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) { // Request was succeed
                if(response.isSuccessful) {
                    Log.i("info", "Succeed in GET SCREEN SHOT!!!")
                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread {
                        // show the image from the simulator
                        val imageView =
                            findViewById<View>(R.id.image_simulator) as ImageView
                        //imageView.setImageResource(R.drawable.ic_launcher_background);
                        imageView.setImageBitmap(B)
                        // image_simulator.setImageBitmap(B)
                    }
                }
                else {
                    if (toast != null) {
                        toast?.cancel();
                    }
                    else {
                        toast =
                            Toast.makeText(
                                applicationContext,
                                "Error in Simulator, go back to login screen",
                                Toast.LENGTH_LONG
                            )
                        toast?.show()
                        //Log.i("info", "Outside isSuccessful response block in Screen Shot")
                    }
                }

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Request was not succeed
                //Log.i("info", "Request was not succeed IN SCREENSHOT!")
            }
        })

    }

    /**
     * send command instance with all the data to the server
     */
    private fun sendDataToServer(s : String) {

        var c =  Command(currentThrottle, currentRudder, 0.0, 0.0)
        try {
            checkObject?.sendNetworkRequest(c, s)
        } catch (e : Exception) {

        }
    }

}