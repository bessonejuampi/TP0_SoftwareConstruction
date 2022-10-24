package com.utn.tp_rifas.common

class DataValidator(
    var nameError:String? = null,
    var lastNameError:String? = null,
    var raffleNumberError:String? =null
){
    fun isSuccessfully():Boolean{
        return nameError.isNullOrEmpty() && lastNameError.isNullOrEmpty() && raffleNumberError.isNullOrEmpty()
    }
}