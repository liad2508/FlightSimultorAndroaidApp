package com.example.flightmobileapp

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * create the Room Database
 */
@Database (entities = [(URL_Entity::class)], version = 1)
abstract class AppDB : RoomDatabase(){
    abstract fun urlDAO() : URL_DAO
}