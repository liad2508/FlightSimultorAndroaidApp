package com.example.flightmobileapp

interface ObservableI {
    fun addToObserver(obs: ObserverI)
    fun notifyObservers(x: Float, y: Float)
}