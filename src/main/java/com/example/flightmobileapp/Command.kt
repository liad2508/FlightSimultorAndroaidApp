package com.example.flightmobileapp

import com.google.gson.annotations.SerializedName

data class Command (
    @SerializedName("user_throttle") val throttle: Double,
    @SerializedName("user_rudder") val rudder: Double,
    @SerializedName("user_aileron") val aileron: Double,
    @SerializedName("user_elevator") val elevator: Double
)