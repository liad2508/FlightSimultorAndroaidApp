package com.example.flightmobileapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

//import android.support.v7.app.AppCompatActivity;

class JoyStick : SecondActivity(), ObserverI {
    // Fileds
    var callServerObj : callServer? = null // for the post command
    var changeInAileron : Double = 0.0
    var lastAileron : Double = 0.0
    var relativeChangeAileron: Double = 0.02
    var changeInElevator : Double = 0.0
    var lastElevator : Double = 0.0
    var relativeChangeElevator: Double = 0.02


    /**
     * initializing values while creating joystick
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //setContentView(R.layout.joystick);
        callServerObj = callServer(applicationContext)
        // getting the joystick activity
        //val intent = intent
        val joyStickView = JoyStickView(this)
        joyStickView.addToObserver(this)
        val rootLayout = findViewById<ViewGroup>(R.id.linear_layout)
        rootLayout.addView(joyStickView)

        //setContentView(joyStickView);
    }

    /**
     * handle change in configuration
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    /**
     * calculate and update the server with the new value of the joystick
     */
    override fun update(x: Float, y: Float) {
        var totalAileron = 0.0
        var totalElevator = 0.0
        if (x > 0) {
            totalAileron = x.toDouble() + 0.24
        }
        if (x < 0) {
            totalAileron = x.toDouble() - 0.24
        }
        if (y > 0) {
            totalElevator = y.toDouble() + 0.3
        }
        if (y < 0) {
            totalElevator = y.toDouble() - 0.3
        }


        var newThrottle  = currentThrottle
        var newRudder = currentRudder

        changeInAileron = totalAileron - lastAileron
        if (changeInAileron < 0) {
            changeInAileron *= -1
        }

        changeInElevator = totalElevator - lastElevator
        if (changeInElevator < 0) {
            changeInElevator *= -1
        }

        // check if there is change in more than 1 % in joystick values
        if (changeInAileron > relativeChangeAileron || changeInElevator > relativeChangeElevator) {
            // get the url of the server
            val chosenURL : String = intent.getStringExtra("url")
            // create command and send to server
            Log.i("Info", "aileron value is $totalAileron and value elevator is $totalElevator")
            val cmd =  Command(newThrottle, newRudder, totalAileron, totalElevator)
            GlobalScope.launch {
                try {
                    callServerObj?.sendNetworkRequest(cmd, chosenURL)
                } catch (e : Exception) {

                }
            }
        }

        // update the last value of the joystick
        lastAileron = totalAileron
        lastElevator = totalElevator

    }
}