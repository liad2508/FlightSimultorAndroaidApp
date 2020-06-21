package com.example.flightmobileapp

import android.content.Context
import android.icu.util.TimeUnit
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.wait

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class callServer {

    var context : Context? = null
    var toast : Toast? = null

    constructor(context: Context?) {
        this.context = context
    }

    /**
     * get command and url and update the server with post request
     */
    fun sendNetworkRequest(cmd: Command, url : String) : Double {
        var check = 0.0
        try {
            // define timeout to read the data
            var okHttpClient: OkHttpClient? = OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            // create retrofit request
            val builder = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            val retrofit = builder.build()
            val client = retrofit.create(CommandApi::class.java)
            if (!cmd.aileron.isFinite()) {
                cmd.aileron = 0.0
            }
            if (!cmd.rudder.isFinite()) {
                cmd.rudder = 0.0
            }
            if (!cmd.elevator.isFinite()) {
                cmd.elevator = 0.0
            }
            if (!cmd.throttle.isFinite()) {
                cmd.throttle = 0.0
            }
            val call = client.createAccount(cmd)

            // check if the post succeed or not
            call!!.enqueue(object : Callback<Command?> {
                override fun onResponse(
                    call: Call<Command?>,
                    response: Response<Command?>
                ) {
                    //Log.i("info", "server was open!!! P O S T")
                }

                override fun onFailure(
                    call: Call<Command?>,
                    t: Throwable
                ) {
                    if (t.toString().contains("failed to connect", ignoreCase = true)) {
                        if (toast != null) {
                            toast?.cancel();
                        }
                        else {
                            toast =
                                Toast.makeText(
                                    context,
                                    "Error in connection, go back to login screen",
                                    Toast.LENGTH_SHORT
                                )
                            toast?.show()
                        }
                    }

                }
            })
        } catch (e : Exception) {

           // Log.e( "catch in callServer: ",e.toString());
            check = -1.0
        }
       return check
    }

}