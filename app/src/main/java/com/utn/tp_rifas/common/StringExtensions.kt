package com.utn.tp_rifas.common


fun String?.isNumber(): Boolean {
    return if (this.isNullOrEmpty()) {
        false
    } else {
        try {
            this.toDouble()
            true
        } catch (e: Exception) {
            false
        }
    }
}
fun String?.validateText(): Boolean {
    return !this.isNullOrEmpty()
}
