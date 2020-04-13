package com.example.paymeapp.util

import android.widget.EditText
import java.math.RoundingMode

fun Double.round() =
    round(this)

fun EditText.round() =
    round(this.text)

fun <T> round(obj : T) :Double =
    obj.toString().toBigDecimal().setScale(2, RoundingMode.UP).toDouble()