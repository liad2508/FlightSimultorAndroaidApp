package com.example.flightmobileapp

import com.google.gson.annotations.SerializedName

/**
 * data class with all the information that need to be sent to the server
 */
data class Command (
    @SerializedName("throttle") var throttle: Double,
    @SerializedName("rudder") var rudder: Double,
    @SerializedName("aileron") var aileron: Double,
    @SerializedName("elevator") var elevator: Double
)