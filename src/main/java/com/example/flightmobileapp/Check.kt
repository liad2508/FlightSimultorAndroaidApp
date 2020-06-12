package com.example.flightmobileapp

import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Check {

    fun sendNetworkRequest(cmd: Command) {
        val builder = Retrofit.Builder()
            .baseUrl("https://localhost:44355/api/Command/")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val client = retrofit.create(CommandApi::class.java)

        /*
        var s : String = "{\n" +
                "\t\t\"aileron\": " + cmd.aileron + ",\n" +
                "\t\t\"rudder\": " + cmd.rudder + ",\n" +
                "\t\t\"elevator\": " + cmd.elevator + ",\n" +
                "\t\t\"throttle\": " + cmd.throttle + "\n" +
                "}"
        var rb : RequestBody = s.toRequestBody("application/json".toMediaTypeOrNull())


         val cl = OkHttpClient();*/





        val call = client.createAccount(cmd)
        call!!.enqueue(object : Callback<Command?> {
            override fun onResponse(
                call: Call<Command?>,
                response: Response<Command?>
            ) {
            }

            override fun onFailure(
                call: Call<Command?>,
                t: Throwable
            ) {
                Log.i("info", "server not open")
            }
        })
    }
}