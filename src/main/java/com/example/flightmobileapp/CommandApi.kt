package com.example.flightmobileapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CommandApi {
    @Headers("Content-Type: application/json")
    @POST("api/Command")
    fun createAccount(@Body command: Command?): Call<Command?>?
}