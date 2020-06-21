package com.example.flightmobileapp

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * interface to send request to the server
 */
interface Api {
    @GET("api/Command")
    fun getImg(): retrofit2.Call<ResponseBody>

    @POST("/post")
    fun postCmd(@Body command: Command?): retrofit2.Call<Command?>?
}
