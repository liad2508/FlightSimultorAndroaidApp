package com.example.flightmobileapp

import com.google.gson.annotations.SerializedName

data class Command (
    @SerializedName("throttle") val throttle: Double,
    @SerializedName("rudder") val rudder: Double,
    @SerializedName("aileron") val aileron: Double,
    @SerializedName("elevator") val elevator: Double
)