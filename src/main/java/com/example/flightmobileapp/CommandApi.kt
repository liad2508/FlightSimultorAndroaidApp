package com.example.flightmobileapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * interface to communicate with the server with retrofit
 */
interface CommandApi {
    @Headers("Content-Type: application/json")
    @POST("api/Command")
    fun createAccount(@Body command: Command?): Call<Command?>?
}