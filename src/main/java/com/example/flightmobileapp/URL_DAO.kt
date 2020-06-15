package com.example.flightmobileapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface URL_DAO {

    @Insert
    fun saveUrl(url : URL_Entity)

    @Update
    fun updateUrl(url : URL_Entity)

    @Query("select * from URL_Entity")
    fun readUrl() : List<URL_Entity>

}