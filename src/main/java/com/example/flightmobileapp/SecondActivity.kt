package com.example.flightmobileapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Placeholder
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_second.*
import retrofit2.Retrofit

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
    var checkObject = Check()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        //

        //setContentView(R.layout.joystick);

        // getting the joystick activity

/*
        Log.i("info", "create circles");
        var joyStickView =  JoyStickView(this);
        joyStickView.addToObserver(JoyStick());
        setContentView(joyStickView);*/





        //


/*
        val intent = Intent(this, JoyStick::class.java)

        startActivity(intent)



        var JoyStick = JoyStick()
        JoyStick.onCreate(savedInstanceState)

        linearLayout = findViewById<LinearLayout>(R.id.linear_layout)
        layoutInflater.inflate(R.layout.joystick, linearLayout);*/

        Log.i("info", "create second activity");


        throttleSeekBar = findViewById<SeekBar>(R.id.throttle)
        rudderSeekBar = findViewById<SeekBar>(R.id.rudder)



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
                    sendDataToServer()

                }


                Log.i("info", currentRudder.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        if (isChange) {
            var command = Command(currentThrottle, currentRudder, 0.0, 0.1)
            checkObject.sendNetworkRequest(command)
            isChange = false
        }
    }

    private fun sendDataToServer() {
        var command = Command(currentThrottle, currentRudder, 0.0, 0.1)
        checkObject.sendNetworkRequest(command)
    }


    private fun sendNetworkRequest(command: Command)  {

    }

    private fun updateImageFromSimulator(view: View) {

        Log.i("info", "change image")
        var image : ImageView = findViewById<ImageView>(R.id.image_simulator)
        //image.setImageResource(R.drawable.//name of image)
    }

}