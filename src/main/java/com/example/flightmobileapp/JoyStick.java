package com.example.flightmobileapp;



import android.content.Intent;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class JoyStick extends SecondActivity implements ObserverI {


    /**
     * initializing values while creating joystick
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        Log.i("info", "create circlesssssssssss");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.joystick);

        // getting the joystick activity
        Intent intent = getIntent();

        Log.i("info", "create circles");
        JoyStickView joyStickView = new JoyStickView(this);
        joyStickView.addToObserver(this);

        ViewGroup rootLayout = findViewById(R.id.linear_layout);

        rootLayout.addView(joyStickView);



        //setContentView(joyStickView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void update(float x, float y) {

    }
}