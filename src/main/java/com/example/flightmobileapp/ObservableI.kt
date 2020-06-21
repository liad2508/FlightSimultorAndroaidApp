package com.example.flightmobileapp

/**
 * observable interface
 */
interface ObservableI {
    fun addToObserver(obs: ObserverI)
    fun notifyObservers(x: Float, y: Float)
}