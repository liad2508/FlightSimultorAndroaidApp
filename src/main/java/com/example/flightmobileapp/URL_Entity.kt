package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
/**
 * Entity class with the columns in the db
 */
class URL_Entity {

    @PrimaryKey
    var URL_id : Int = 0

    @ColumnInfo (name = "URL_NAME")
    var URL_name : String = ""

}