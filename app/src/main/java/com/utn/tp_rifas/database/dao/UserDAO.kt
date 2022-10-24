package com.utn.tp_rifas.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.utn.tp_rifas.model.User

@Dao
interface UserDAO {

    @Query("SELECT raffleNumber FROM user WHERE raffleNumber=:raffleNumber")
    fun getRaffleNumber(raffleNumber:String):String

    @Insert
    fun saveUser(user:User)
}