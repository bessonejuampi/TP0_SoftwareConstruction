package com.utn.tp_rifas.viewmodel

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utn.tp_rifas.R
import com.utn.tp_rifas.common.*
import com.utn.tp_rifas.model.User
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RaffleViewModel(private var context: Context) : ViewModel() {

    var dataValidationMutable = MutableLiveData<DataValidator?>()
    var listRifas : ArrayList<User> = ArrayList()

    fun validateInputs(
        name: String?,
        lastName: String?,
        raffleNumber: String?,
        fechaDeCompra: String?,
        horaDeCompra: String?
    ) {
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

            if (!esHoraDeCompraValida(horaDeCompra!!)) {
                dataValidator.horaDeCompraError = context.getString(R.string.textErrorInput)
            }

            if (!esFechaDeCompraValida(fechaDeCompra!!)) {
                dataValidator.fechaDeCompraError = context.getString(R.string.text_error_input_fecha_de_compra)
            }

            if (dataValidator.isSuccessfully()) {

                if (validateRaffleNumber(raffleNumber!!)) {
                    saveUser(
                        User(
                            name!!,
                            lastName!!,
                            raffleNumber,
                            fechaDeCompra!!,
                            horaDeCompra!!
                        )
                    )
                    showAlertDialog(
                        "El número $raffleNumber ha sido asignado a $name $lastName",
                        context.getString(
                            R.string.succes_message_title
                        )
                    )
                }
            }
            dataValidationMutable.value = dataValidator
        }
    }

    private fun saveUser(user: User) {
        listRifas.add(user)
    }

    private fun validateRaffleNumber(raffleNumber: String): Boolean {
        var response = false
        if (isBetweenAcceptableLimit(raffleNumber)) {
            if (!raffleNumberAlreadyExists(raffleNumber)) {
                response = true
            } else {
                showAlertDialog(
                    context.getString(R.string.text_alert_dialog_repeated_number),
                    context.getString(
                        R.string.title_alert_dialog
                    )
                )
            }
        } else {
            showAlertDialog(
                context.getString(R.string.text_alert_dialog_error_liimit), context.getString(
                    R.string.title_alert_dialog
                )
            )
        }
        return response
    }

    private fun isBetweenAcceptableLimit(raffleNumber: String): Boolean {
        return raffleNumber.toInt() > Constants.LOWER_LIMIT_RAFFLE_NUMBER &&
                raffleNumber.toInt() < Constants.UPPER_LIMIT_RAFFLE_NUMBER

    }

    private fun raffleNumberAlreadyExists(raffleNumber: String): Boolean {
        val rifasVendidas = getRifasVendidas()
        val listRifasVendidas: JSONArray = JSONArray(rifasVendidas)
        var response = false
        for (i in 0 until listRifasVendidas.length()) {
            var rifa: JSONObject = listRifasVendidas.getJSONObject(i)
            if (rifa.getString("numero_rifa").equals(raffleNumber)) {
                response = true
            }
        }
        return response
    }

    private fun getRifasVendidas(): String? {
        var rifasVendidas: String? = ""
        try {
            val stream: InputStream = context.assets.open("archivo.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            rifasVendidas = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rifasVendidas
    }


    private fun showAlertDialog(message: String, title: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
            }
        val alertDialog = builder.create()
        alertDialog.show()

    }


   private fun esFechaDeCompraValida(fechaDeCompra: String) : Boolean {
        var retrono = false

        val fechaActual = Date(System.currentTimeMillis())
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
        try {
            val fechaAValidar: Date = formatoFecha.parse(fechaDeCompra)!!
            if (fechaAValidar.before(fechaActual)) {
                retrono = true
            }
        }catch (e:Exception){
            Toast.makeText(context,context.getString(R.string.text_toast_fecha_invalida),Toast.LENGTH_LONG).show()
        }
        return retrono
    }

    private fun esHoraDeCompraValida(horaDeCompra: String):Boolean{
        var respuesta = false
        try{
            var calendar : Calendar = Calendar.getInstance()
            var dateFormat : SimpleDateFormat = SimpleDateFormat("HH:mm")
            var horaAValidar : Date = dateFormat.parse(horaDeCompra)
            respuesta = true
        }catch (e:Exception){
            Toast.makeText(context, "Hora invalida", Toast.LENGTH_LONG).show()
        }
        return respuesta
    }
}
