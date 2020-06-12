package com.example.flightmobileapp;

public interface ObservableI {

    void addToObserver(ObserverI obs);
    void notifyObservers(float x, float y);
}