package com.example.flightmobileapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText

//import android.support.v7.app.AppCompatActivity;

class JoyStick : SecondActivity(), ObserverI {
    // Fileds
    var callServerObj = callServer() // for the post command.


    /**
     * initializing values while creating joystick
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("info", "create circlesssssssssss")
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.joystick);

        // getting the joystick activity
        val intent = intent
        Log.i("info", "create circles")
        val joyStickView = JoyStickView(this)
        joyStickView.addToObserver(this)
        val rootLayout = findViewById<ViewGroup>(R.id.linear_layout)
        rootLayout.addView(joyStickView)


        //setContentView(joyStickView);
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

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

        Log.i("Info", "in joystick x is ${x} and y is ${y}")
        var a = findViewById<EditText>(R.id.elevator_check)
        a.setText(totalElevator.toString())
        var newThrottle  = currentThrottle
        var newRudder = currentRudder

        val cmd =  Command(newThrottle, newRudder, totalAileron, totalElevator)
        callServerObj.sendNetworkRequest(cmd)

    }
}