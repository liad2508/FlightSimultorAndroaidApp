package com.example.flightmobileapp

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
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    var throttleSeekBar : SeekBar? = null
    var rudderSeekBar : SeekBar? = null
    var currentThrottle: Double = 0.0
    var lastThrottle: Double = 0.0
    var currentRudder: Double = 0.0
    var lastRudder: Double = 0.0
    var relativeChangeThrottle : Double = 0.01


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


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
                    Log.i("info", "change in more than 1 %")
                    // send request to server
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
                Log.i("info", currentRudder.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun updateImageFromSimulator(view: View) {
        Log.i("info", "change image")

        var image : ImageView = findViewById<ImageView>(R.id.image_simulator)
        //image.setImageResource(R.drawable.//name of image)
    }


}