package com.utn.tp_rifas.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User(
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "lastName") var lastName:String,
    @ColumnInfo(name = "raffleNumber") var raffleNumber:String
) {
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}