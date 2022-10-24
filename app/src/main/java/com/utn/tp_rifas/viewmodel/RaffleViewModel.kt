package com.utn.tp_rifas.viewmodel

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utn.tp_rifas.R
import com.utn.tp_rifas.common.*
import com.utn.tp_rifas.database.AppDatabase
import com.utn.tp_rifas.model.User
import kotlinx.coroutines.launch

class RaffleViewModel(private var context: Context) : ViewModel() {

    var dataValidationMutable = MutableLiveData<DataValidator?>()

    fun validateInputs(name: String?, lastName: String?, raffleNumber: String?) {
        viewModelScope.launch {
            val dataValidator = DataValidator()
            if (!name.validateText()) {
                dataValidator.nameError = context.getString(R.string.textErrorInput)
            }
            if (!lastName.validateText()) {
                dataValidator.lastNameError = context.getString(R.string.textErrorInput)
            }
            if (!raffleNumber.isNumber()) {
                dataValidator.raffleNumberError = context.getString(R.string.textErrorInput)
            }
            if (dataValidator.isSuccessfully()) {
                if (validateRaffleNumber(raffleNumber!!)) {
                    saveUser(User(name!!, lastName!!, raffleNumber))
                    showAlertDialog("El número $raffleNumber ha sido asignado a $name $lastName"  , context.getString(
                                            R.string.succes_message_title))
                }
            }
            dataValidationMutable.value = dataValidator
        }
    }

    private fun saveUser(user: User){
        val database = AppDatabase.getInstance(context)?.UserDAO()
        database?.saveUser(user)
    }

    private fun validateRaffleNumber(raffleNumber: String): Boolean {
        var response = false
        if (isBetweenAcceptableLimit(raffleNumber)) {
            if (!raffleNumberAlreadyExists(raffleNumber)) {
                response = true
            }else{
                showAlertDialog(context.getString(R.string.text_alert_dialog_repeated_number),context.getString(
                                    R.string.title_alert_dialog))
            }
        }else{
            showAlertDialog(context.getString(R.string.text_alert_dialog_error_liimit), context.getString(
                R.string.title_alert_dialog))
        }
        return response
    }

    private fun isBetweenAcceptableLimit(raffleNumber: String): Boolean {
        return raffleNumber.toInt() > Constants.LOWER_LIMIT_RAFFLE_NUMBER &&
                raffleNumber.toInt() < Constants.UPPER_LIMIT_RAFFLE_NUMBER

    }

    private fun raffleNumberAlreadyExists(raffleNumber: String): Boolean {
        val database = AppDatabase.getInstance(context)?.UserDAO()
        return !database?.getRaffleNumber(raffleNumber).isNullOrEmpty()
    }

    private fun showAlertDialog(message : String, title:String){
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK"){
                    dialog, which -> dialog.cancel()
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }
}